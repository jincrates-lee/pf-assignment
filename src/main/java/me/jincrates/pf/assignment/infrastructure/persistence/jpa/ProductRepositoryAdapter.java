package me.jincrates.pf.assignment.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.domain.vo.PageSize;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ProductJpaEntity;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper.ProductJpaMapper;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository.ProductJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository repository;

    @Override
    public Product save(final Product product) {
        ProductJpaEntity entity = ProductJpaMapper.toEntity(product);
        ProductJpaEntity saved = repository.save(entity);
        return ProductJpaMapper.toDomain(saved);
    }

    @Override
    public Product update(final Product newProduct) {
        if (newProduct.id() == null) {
            throw new BusinessException("상품 ID가 잘못되었습니다.");
        }
        return save(newProduct);
    }

    @Override
    public boolean existsById(final Long productId) {
        return repository.existsById(productId);
    }

    @Override
    public Optional<Product> findById(final Long productId) {
        return repository.findById(productId)
            .map(ProductJpaMapper::toDomain);
    }

    @Override
    public void deleteById(final Long productId) {
        repository.deleteById(productId);
    }

    @Override
    public List<Product> findAllByCategoryId(
        final Long categoryId,
        final String sort,
        final PageSize pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(
            pageSize.page(),
            pageSize.size() + 1,
            sortBy(sort)
        );

        return repository.findAllByCategoryId(
                categoryId,
                pageRequest
            ).stream()
            .map(ProductJpaMapper::toDomain)
            .toList();
    }

    private Sort sortBy(final String sortParam) {
        return switch (sortParam) {
            case "price_asc" -> Sort.by("discountPrice").ascending();  // 낮은 가격순
            case "review_desc" -> Sort.by("discountPrice").descending();  // 리뷰 많은 순
            default -> Sort.by("discountPrice").ascending(); // 기본값을 낮은 가격 순으로 설정
        };
    }
}
