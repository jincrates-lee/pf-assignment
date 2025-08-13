package me.jincrates.pf.assignment.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import me.jincrates.pf.assignment.application.dto.CreateProductRequest;
import me.jincrates.pf.assignment.application.dto.CreateProductResponse;
import me.jincrates.pf.assignment.application.dto.GetAllProductsQuery;
import me.jincrates.pf.assignment.application.dto.ProductSummaryResponse;
import me.jincrates.pf.assignment.application.dto.UpdateProductRequest;
import me.jincrates.pf.assignment.application.dto.UpdateProductResponse;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.application.repository.ReviewRepository;
import me.jincrates.pf.assignment.domain.event.ProductDeleted;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Category;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.domain.vo.PageSize;
import me.jincrates.pf.assignment.domain.vo.ProductSortType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductUseCase 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProductService productService;

    private Category parentCategory;
    private Category childCategory;
    private Product product;
    private CreateProductRequest createRequest;
    private UpdateProductRequest updateRequest;
    private GetAllProductsQuery query;

    @BeforeEach
    void setUp() {
        parentCategory = Category.builder()
            .id(1L)
            .name("반려동물")
            .depth(1)
            .parent(null)
            .build();

        childCategory = Category.builder()
            .id(2L)
            .name("강아지")
            .depth(2)
            .parent(parentCategory)
            .build();

        product = Product.builder()
            .id(1L)
            .name("촉촉트릿 북어 80g")
            .sellingPrice(15000L)
            .discountPrice(2100L)
            .brand("촉촉트릿")
            .categories(List.of(
                parentCategory,
                childCategory
            ))
            .build();

        createRequest = new CreateProductRequest(
            "촉촉트릿 북어 80g",
            15000L,
            2100L,
            "촉촉트릿",
            Set.of(
                1L,
                2L
            )
        );

        updateRequest = new UpdateProductRequest(
            1L,
            "업데이트된 상품명",
            20000L,
            3000L,
            "업데이트된 브랜드",
            Set.of(
                1L,
                2L
            )
        );

        query = new GetAllProductsQuery(
            1L,
            ProductSortType.REVIEW_COUNT_DESC,
            new PageSize(
                0,
                20
            )
        );
    }

    @Nested
    @DisplayName("상품 생성 테스트")
    class CreateProductTest {

        @Test
        @DisplayName("정상적으로 상품을 생성한다")
        void shouldCreateProductSuccessfully() {
            // given
            List<Category> categories = List.of(
                parentCategory,
                childCategory
            );
            Product savedProduct = Product.builder()
                .id(1L)
                .name("촉촉트릿 북어 80g")
                .sellingPrice(15000L)
                .discountPrice(2100L)
                .brand("촉촉트릿")
                .categories(categories)
                .build();

            given(categoryRepository.findAllByIdIn(createRequest.categoryIds()))
                .willReturn(categories);
            given(productRepository.save(any(Product.class)))
                .willReturn(savedProduct);

            // when
            CreateProductResponse response = productService.create(createRequest);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.productId()
            );

            verify(categoryRepository).findAllByIdIn(createRequest.categoryIds());
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("카테고리가 존재하지 않으면 예외가 발생한다")
        void shouldThrowExceptionWhenCategoriesNotFound() {
            // given
            given(categoryRepository.findAllByIdIn(createRequest.categoryIds()))
                .willReturn(List.of());

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.create(createRequest)
            );

            assertEquals(
                "카테고리를 찾을 수 없습니다.",
                exception.getMessage()
            );
            verify(categoryRepository).findAllByIdIn(createRequest.categoryIds());
            verify(
                productRepository,
                never()
            ).save(any(Product.class));
        }

        @Test
        @DisplayName("일부 카테고리가 유효하지 않으면 예외가 발생한다")
        void shouldThrowExceptionWhenSomeCategoriesAreInvalid() {
            // given
            List<Category> categories = List.of(parentCategory); // 하나만 반환
            given(categoryRepository.findAllByIdIn(createRequest.categoryIds()))
                .willReturn(categories);

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.create(createRequest)
            );

            assertEquals(
                "유효하지 않은 카테고리가 포함되어 있습니다.",
                exception.getMessage()
            );
            verify(categoryRepository).findAllByIdIn(createRequest.categoryIds());
            verify(
                productRepository,
                never()
            ).save(any(Product.class));
        }
    }

    @Nested
    @DisplayName("상품 수정 테스트")
    class UpdateProductTest {

        @Test
        @DisplayName("정상적으로 상품을 수정한다")
        void shouldUpdateProductSuccessfully() {
            // given
            List<Category> categories = List.of(
                parentCategory,
                childCategory
            );
            Product updatedProduct = Product.builder()
                .id(1L)
                .name("업데이트된 상품명")
                .sellingPrice(20000L)
                .discountPrice(3000L)
                .brand("업데이트된 브랜드")
                .categories(categories)
                .build();

            given(productRepository.findById(updateRequest.id()))
                .willReturn(Optional.of(product));
            given(categoryRepository.findAllByIdIn(updateRequest.categoryIds()))
                .willReturn(categories);
            given(productRepository.update(any(Product.class)))
                .willReturn(updatedProduct);

            // when
            UpdateProductResponse response = productService.update(updateRequest);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.productId()
            );

            verify(productRepository).findById(updateRequest.id());
            verify(categoryRepository).findAllByIdIn(updateRequest.categoryIds());
            verify(productRepository).update(any(Product.class));
        }

        @Test
        @DisplayName("상품이 존재하지 않으면 예외가 발생한다")
        void shouldThrowExceptionWhenProductNotFound() {
            // given
            given(productRepository.findById(updateRequest.id()))
                .willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.update(updateRequest)
            );

            assertEquals(
                "상품을 찾을 수 없습니다.",
                exception.getMessage()
            );
            verify(productRepository).findById(updateRequest.id());
            verify(
                productRepository,
                never()
            ).update(any(Product.class));
        }

        @Test
        @DisplayName("카테고리 ID가 null이면 기존 카테고리를 유지한다")
        void shouldKeepExistingCategoriesWhenCategoryIdsIsNull() {
            // given
            UpdateProductRequest requestWithoutCategories = new UpdateProductRequest(
                1L,
                "업데이트된 상품명",
                20000L,
                3000L,
                "업데이트된 브랜드",
                null
            );

            Product updatedProduct = Product.builder()
                .id(1L)
                .name("업데이트된 상품명")
                .sellingPrice(20000L)
                .discountPrice(3000L)
                .brand("업데이트된 브랜드")
                .categories(product.categories())
                .build();

            given(productRepository.findById(requestWithoutCategories.id()))
                .willReturn(Optional.of(product));
            given(productRepository.update(any(Product.class)))
                .willReturn(updatedProduct);

            // when
            UpdateProductResponse response = productService.update(requestWithoutCategories);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.productId()
            );

            verify(productRepository).findById(requestWithoutCategories.id());
            verify(
                categoryRepository,
                never()
            ).findAllByIdIn(any());
            verify(productRepository).update(any(Product.class));
        }
    }

    @Nested
    @DisplayName("상품 삭제 테스트")
    class DeleteProductTest {

        @Test
        @DisplayName("정상적으로 상품을 삭제한다")
        void shouldDeleteProductSuccessfully() {
            // given
            Long productId = 1L;
            given(productRepository.existsById(productId))
                .willReturn(true);

            // when
            productService.delete(productId);

            // then
            verify(productRepository).existsById(productId);
            verify(productRepository).deleteById(productId);

            ArgumentCaptor<ProductDeleted> eventCaptor = ArgumentCaptor.forClass(ProductDeleted.class);
            verify(eventPublisher).publishEvent(eventCaptor.capture());

            ProductDeleted capturedEvent = eventCaptor.getValue();
            assertEquals(
                productId,
                capturedEvent.productId()
            );
            assertNotNull(capturedEvent.occurredAt());
        }

        @Test
        @DisplayName("상품이 존재하지 않으면 예외가 발생한다")
        void shouldThrowExceptionWhenProductNotFound() {
            // given
            Long productId = 1L;
            given(productRepository.existsById(productId))
                .willReturn(false);

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.delete(productId)
            );

            assertEquals(
                "상품을 찾을 수 없습니다.",
                exception.getMessage()
            );
            verify(productRepository).existsById(productId);
            verify(
                productRepository,
                never()
            ).deleteById(productId);
            verify(
                eventPublisher,
                never()
            ).publishEvent(any());
        }
    }

    @Nested
    @DisplayName("카테고리별 상품 조회 테스트")
    class GetAllProductsByCategoryIdTest {

        @Test
        @DisplayName("정상적으로 카테고리별 상품 목록을 조회한다")
        void shouldGetAllProductsByCategoryIdSuccessfully() {
            // given
            List<Product> products = List.of(product);
            Map<Long, Double> averageScoreMap = Map.of(
                1L,
                4.5
            );

            given(productRepository.findAllByCategoryId(
                query.categoryId(),
                query.sort(),
                query.pageSize()
            )).willReturn(products);
            given(reviewRepository.findAverageScoreByProductIdIn(List.of(1L)))
                .willReturn(averageScoreMap);

            // when
            List<ProductSummaryResponse> responses = productService.getAllProductsByCategoryId(query);

            // then
            assertNotNull(responses);
            assertEquals(
                1,
                responses.size()
            );

            ProductSummaryResponse response = responses.get(0);
            assertEquals(
                1L,
                response.id()
            );
            assertEquals(
                "촉촉트릿 북어 80g",
                response.name()
            );
            assertEquals(
                15000L,
                response.sellingPrice()
            );
            assertEquals(
                2100L,
                response.discountPrice()
            );
            assertEquals(
                "촉촉트릿",
                response.brand()
            );
            assertEquals(
                4.5,
                response.reviewAverageScore()
            );
            assertEquals(
                2,
                response.categories().size()
            );

            verify(productRepository).findAllByCategoryId(
                query.categoryId(),
                query.sort(),
                query.pageSize()
            );
            verify(reviewRepository).findAverageScoreByProductIdIn(List.of(1L));
        }

        @Test
        @DisplayName("리뷰 점수가 없는 상품은 0.0으로 설정된다")
        void shouldSetZeroScoreWhenNoReviews() {
            // given
            List<Product> products = List.of(product);
            Map<Long, Double> averageScoreMap = Map.of(); // 빈 맵

            given(productRepository.findAllByCategoryId(
                query.categoryId(),
                query.sort(),
                query.pageSize()
            )).willReturn(products);
            given(reviewRepository.findAverageScoreByProductIdIn(List.of(1L)))
                .willReturn(averageScoreMap);

            // when
            List<ProductSummaryResponse> responses = productService.getAllProductsByCategoryId(query);

            // then
            assertNotNull(responses);
            assertEquals(
                1,
                responses.size()
            );

            ProductSummaryResponse response = responses.get(0);
            assertEquals(
                0.0,
                response.reviewAverageScore()
            );

            verify(productRepository).findAllByCategoryId(
                query.categoryId(),
                query.sort(),
                query.pageSize()
            );
            verify(reviewRepository).findAverageScoreByProductIdIn(List.of(1L));
        }

        @Test
        @DisplayName("상품이 없으면 빈 목록을 반환한다")
        void shouldReturnEmptyListWhenNoProducts() {
            // given
            List<Product> products = List.of();
            Map<Long, Double> averageScoreMap = Map.of();

            given(productRepository.findAllByCategoryId(
                query.categoryId(),
                query.sort(),
                query.pageSize()
            )).willReturn(products);
            given(reviewRepository.findAverageScoreByProductIdIn(List.of()))
                .willReturn(averageScoreMap);

            // when
            List<ProductSummaryResponse> responses = productService.getAllProductsByCategoryId(query);

            // then
            assertNotNull(responses);
            assertEquals(
                0,
                responses.size()
            );

            verify(productRepository).findAllByCategoryId(
                query.categoryId(),
                query.sort(),
                query.pageSize()
            );
            verify(reviewRepository).findAverageScoreByProductIdIn(List.of());
        }
    }

    @Nested
    @DisplayName("카테고리 검증 테스트")
    class ValidateCategoriesTest {

        @Test
        @DisplayName("빈 카테고리 ID 집합으로 호출하면 예외가 발생한다")
        void shouldThrowExceptionWhenCategoryIdsIsEmpty() {
            // given
            Set<Long> emptyCategoryIds = Set.of();
            given(categoryRepository.findAllByIdIn(emptyCategoryIds))
                .willReturn(List.of());

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.create(new CreateProductRequest(
                    "테스트 상품",
                    10000L,
                    1000L,
                    "테스트 브랜드",
                    emptyCategoryIds
                ))
            );

            assertEquals(
                "카테고리를 찾을 수 없습니다.",
                exception.getMessage()
            );
        }
    }
}
