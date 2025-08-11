package me.jincrates.pf.assignment.application.service;

import static me.jincrates.pf.assignment.shared.util.ValueUtil.defaultIfNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.ProductUseCase;
import me.jincrates.pf.assignment.application.dto.CreateProductRequest;
import me.jincrates.pf.assignment.application.dto.CreateProductResponse;
import me.jincrates.pf.assignment.application.dto.GetAllProductsQuery;
import me.jincrates.pf.assignment.application.dto.ProductSummaryResponse;
import me.jincrates.pf.assignment.application.dto.ProductSummaryResponse.CategoryResponse;
import me.jincrates.pf.assignment.application.dto.UpdateProductRequest;
import me.jincrates.pf.assignment.application.dto.UpdateProductResponse;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.application.repository.ReviewRepository;
import me.jincrates.pf.assignment.domain.event.ProductDeleted;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Category;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.domain.vo.ProductSummary;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CreateProductResponse create(final CreateProductRequest request) {
        List<Category> categories = validatedCategories(request.categoryIds());

        Product product = Product.builder()
            .name(request.name())
            .sellingPrice(request.sellingPrice())
            .discountPrice(request.discountPrice())
            .brand(request.brand())
            .categories(categories)
            .build();

        Product saved = productRepository.save(product);

        return CreateProductResponse.builder()
            .productId(saved.id())
            .build();
    }

    @Override
    @Transactional
    public UpdateProductResponse update(final UpdateProductRequest request) {
        Product existing = productRepository.findById(request.id())
            .orElseThrow(() -> new BusinessException(
                "상품을 찾을 수 없습니다.",
                Map.of(
                    "productId",
                    request.id()
                )
            ));

        List<Category> categories = existing.categories();
        if (request.categoryIds() != null) {
            categories = validatedCategories(request.categoryIds());
        }

        Product product = Product.builder()
            .id(request.id())
            .name(defaultIfNull(
                request.name(),
                existing.name()
            ))
            .sellingPrice(defaultIfNull(
                request.sellingPrice(),
                existing.sellingPrice()
            ))
            .discountPrice(defaultIfNull(
                request.discountPrice(),
                existing.discountPrice()
            ))
            .brand(defaultIfNull(
                request.brand(),
                existing.brand()
            ))
            .categories(categories)
            .build();

        Product updated = productRepository.update(product);

        return UpdateProductResponse.builder()
            .productId(updated.id())
            .build();
    }

    @Override
    @Transactional
    public void delete(final Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new BusinessException(
                "상품을 찾을 수 없습니다.",
                Map.of(
                    "productId",
                    productId
                )
            );
        }

        productRepository.deleteById(productId);

        // 상품 삭제 이벤트 발행
        eventPublisher.publishEvent(
            ProductDeleted.builder()
                .productId(productId)
                .occurredAt(LocalDateTime.now())
                .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSummaryResponse> getAllProductsByCategoryId(final GetAllProductsQuery query) {
        List<Product> products = productRepository.findAllByCategoryId(
            query.categoryId(),
            query.sort(),
            query.pageSize()
        );

        List<Long> productIds = products.stream()
            .map(Product::id)
            .toList();

        Map<Long, Double> productAverageScoreMap = reviewRepository.findAverageScoreByProductIdIn(productIds);

        List<ProductSummary> productSummaries = products.stream()
            .map(product -> {
                double averageScore = productAverageScoreMap.getOrDefault(
                    product.id(),
                    0.0
                );
                return ProductSummary.builder()
                    .id(product.id())
                    .name(product.name())
                    .sellingPrice(product.sellingPrice())
                    .discountPrice(product.discountPrice())
                    .brand(product.brand())
                    .categories(product.categories())
                    .discountRate(product.calculateDiscountRate())
                    .reviewAverageScore(averageScore)
                    .build();
            })
            .toList();

        return productSummaries.stream()
            .map(ProductService::toResponse)
            .toList();
    }

    private List<Category> validatedCategories(final Set<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllByIdIn(categoryIds);
        if (categories.isEmpty()) {
            throw new BusinessException(
                "카테고리를 찾을 수 없습니다.",
                Map.of(
                    "categoryIds",
                    categoryIds
                )
            );
        }

        if (categories.size() != categoryIds.size()) {
            throw new BusinessException(
                "유효하지 않은 카테고리가 포함되어 있습니다.",
                Map.of(
                    "categoryIds",
                    categoryIds
                )
            );
        }

        return categories;
    }

    private static ProductSummaryResponse toResponse(final ProductSummary product) {
        List<CategoryResponse> categories = product.categories().stream()
            .sorted(Comparator.comparing(Category::depth)) // depth 오름차순
            .map(ProductService::toResponse)
            .toList();
        return ProductSummaryResponse.builder()
            .id(product.id())
            .name(product.name())
            .sellingPrice(product.sellingPrice())
            .discountPrice(product.discountPrice())
            .brand(product.brand())
            .categories(categories)
            .discountRate(product.discountRate())
            .reviewAverageScore(product.reviewAverageScore())
            .build();
    }

    private static CategoryResponse toResponse(final Category category) {
        return CategoryResponse.builder()
            .id(category.id())
            .name(category.name())
            .depth(category.depth())
            .parent(category.parent() == null ? null : toResponse(category.parent()))
            .build();
    }
}
