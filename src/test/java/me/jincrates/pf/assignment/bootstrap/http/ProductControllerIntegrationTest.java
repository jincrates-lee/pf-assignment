package me.jincrates.pf.assignment.bootstrap.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.jincrates.pf.assignment.IntegrationTestSupport;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.domain.model.Category;
import me.jincrates.pf.assignment.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Tag("integration")
@DisplayName("상품 API 통합 테스트")
class ProductControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        // 테스트용 카테고리 생성
        category1 = categoryRepository.save(Category.builder()
            .name("사료")
            .depth(1)
            .build());
        category2 = categoryRepository.save(Category.builder()
            .name("간식")
            .depth(1)
            .build());
    }

    @Nested
    @DisplayName("상품 생성 테스트")
    class CreateProductTest {

        @Test
        @DisplayName("유효한 상품 정보로 상품을 생성할 수 있다")
        void createProductWhenValidDataThenSuccess() {
            // given
            String requestBody = String.format(
                """
                    {
                        "name": "프리미엄 사료",
                        "sellingPrice": 50000,
                        "discountPrice": 45000,
                        "brand": "로얄캐닌",
                        "categoryIds": [%d, %d]
                    }
                    """,
                category1.id(),
                category2.id()
            );

            // when
            webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(null)
                .jsonPath("$.data.productId").isNumber();
        }

        @Test
        @DisplayName("상품 이름이 공백일 때 생성에 실패한다")
        void createProduct_WithBlankName_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "name": "",
                        "sellingPrice": 50000,
                        "discountPrice": 45000,
                        "brand": "로얄캐닌",
                        "categoryIds": [%d]
                    }
                    """,
                category1.id()
            );

            // when
            webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품 이름은 필수입니다. 입력된 값: [name=]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("판매가가 0원 이하일 때 생성에 실패한다")
        void createProduct_WithNegativeSellingPrice_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "name": "프리미엄 사료",
                        "sellingPrice": -1000,
                        "discountPrice": 0,
                        "brand": "로얄캐닌",
                        "categoryIds": [%d]
                    }
                    """,
                category1.id()
            );

            // when
            webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품 판매가는 0원보다 커야합니다. 입력된 값: [sellingPrice=-1000]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("할인가가 음수일 때 생성에 실패한다")
        void createProduct_WithNegativeDiscountPrice_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "name": "프리미엄 사료",
                        "sellingPrice": 50000,
                        "discountPrice": -1000,
                        "brand": "로얄캐닌",
                        "categoryIds": [%d]
                    }
                    """,
                category1.id()
            );

            // when
            webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message")
                .isEqualTo("상품 할인가는 0원 이상이여야 합니다. 입력된 값: [discountPrice=-1000]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("브랜드가 공백일 때 생성에 실패한다")
        void createProduct_WithBlankBrand_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "name": "프리미엄 사료",
                        "sellingPrice": 50000,
                        "discountPrice": 45000,
                        "brand": "",
                        "categoryIds": [%d]
                    }
                    """,
                category1.id()
            );

            // when
            webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품 브랜드는 필수입니다. 입력된 값: [brand=]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("카테고리 목록이 비어있을 때 생성에 실패한다")
        void createProduct_WithEmptyCategoryIds_BadRequest() {
            // given
            String requestBody = """
                {
                    "name": "프리미엄 사료",
                    "sellingPrice": 50000,
                    "discountPrice": 45000,
                    "brand": "로얄캐닌",
                    "categoryIds": []
                }
                """;

            // when
            webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품 카테고리 목록은 필수입니다. 입력된 값: [categoryIds=[]]")
                .jsonPath("$.data").isEmpty();
        }
    }

    @Nested
    @DisplayName("상품 수정 테스트")
    class UpdateProductTest {

        private Product savedProduct;

        @BeforeEach
        void setUp() {
            savedProduct = productRepository.save(Product.builder()
                .name("기존 상품")
                .sellingPrice(30000L)
                .discountPrice(25000L)
                .brand("기존 브랜드")
                .categories(List.of(category1))
                .build());
        }

        @Test
        @DisplayName("유효한 상품 정보로 상품을 수정할 수 있다")
        void updateProduct_WithValidData_Success() {
            // given
            String requestBody = String.format(
                """
                    {
                        "name": "수정된 상품",
                        "sellingPrice": 60000,
                        "discountPrice": 55000,
                        "brand": "수정된 브랜드",
                        "categoryIds": [%d, %d]
                    }
                    """,
                category1.id(),
                category2.id()
            );

            // when & then
            webTestClient.patch()
                .uri(
                    "/api/products/{productId}",
                    savedProduct.id()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(null)
                .jsonPath("$.data.productId").isEqualTo(savedProduct.id());
        }

        @Test
        @DisplayName("존재하지 않는 상품 수정 시 실패한다")
        void updateProduct_WithNonExistentProduct_NotFound() {
            // given
            Long nonExistentProductId = 999L;
            String requestBody = String.format(
                """
                    {
                        "name": "수정된 상품",
                        "sellingPrice": 60000,
                        "discountPrice": 55000,
                        "brand": "수정된 브랜드",
                        "categoryIds": [%d]
                    }
                    """,
                category1.id()
            );

            // when
            webTestClient.patch()
                .uri(
                    "/api/products/{productId}",
                    nonExistentProductId
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품을 찾을 수 없습니다.")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("상품 이름이 공백일 때 수정에 실패한다")
        void updateProduct_WithBlankName_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "name": "",
                        "sellingPrice": 60000,
                        "discountPrice": 55000,
                        "brand": "수정된 브랜드",
                        "categoryIds": [%d]
                    }
                    """,
                category1.id()
            );

            // when
            webTestClient.patch()
                .uri(
                    "/api/products/{productId}",
                    savedProduct.id()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품 이름은 필수입니다.")
                .jsonPath("$.data").isEmpty();
        }

    }

    @Nested
    @DisplayName("상품 삭제 테스트")
    class DeleteProductTest {

        private Product savedProduct;

        @BeforeEach
        void setUp() {
            savedProduct = productRepository.save(Product.builder()
                .name("삭제할 상품")
                .sellingPrice(30000L)
                .discountPrice(25000L)
                .brand("테스트 브랜드")
                .categories(List.of(category1))
                .build());
        }

        @Test
        @DisplayName("존재하는 상품을 삭제할 수 있다")
        void deleteProduct_WithExistingProduct_Success() {
            // when & then
            webTestClient.delete()
                .uri(
                    "/api/products/{productId}",
                    savedProduct.id()
                )
                .exchange()
                .expectStatus().isNoContent();

            // 실제로 삭제되었는지 확인
            assertThat(productRepository.findById(savedProduct.id())).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 상품 삭제 시 실패한다")
        void deleteProduct_WithNonExistentProduct_NotFound() {
            // given
            Long nonExistentProductId = 999L;

            // when
            webTestClient.delete()
                .uri(
                    "/api/products/{productId}",
                    nonExistentProductId
                )
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품을 찾을 수 없습니다.")
                .jsonPath("$.data").isEmpty();
        }
    }

    @Nested
    @DisplayName("카테고리 기준 상품 목록 조회 테스트")
    class GetProductListByCategoryTest {

        private Product product1, product2, product3;
        private Category category3;

        @BeforeEach
        void setUpProducts() {
            // 추가 카테고리 생성
            category3 = categoryRepository.save(Category.builder()
                .name("용품")
                .depth(1)
                .build());

            // 테스트용 상품들 생성
            product1 = productRepository.save(Product.builder()
                .name("촉촉트릿 북어 80g")
                .sellingPrice(15000L)
                .discountPrice(2100L)
                .brand("촉촉트릿")
                .categories(List.of(category1)) // 사료 카테고리
                .build());

            product2 = productRepository.save(Product.builder()
                .name("베스트 프라이스 치카루틴 덴탈껌")
                .sellingPrice(22000L)
                .discountPrice(11100L)
                .brand("베스트 프라이스")
                .categories(List.of(category2)) // 간식 카테고리
                .build());

            product3 = productRepository.save(Product.builder()
                .name("로얄캐닌 독 미니 인도어")
                .sellingPrice(48800L)
                .discountPrice(0L)
                .brand("로얄캐닌")
                .categories(List.of(
                    category1,
                    category3
                )) // 사료, 용품 카테고리
                .build());
        }

        @Test
        @DisplayName("카테고리 ID로 상품 목록을 조회할 수 있다")
        void getAllProductsByCategoryId_WithValidCategoryId_Success() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(null)
                .jsonPath("$.data.page").isEqualTo(0)
                .jsonPath("$.data.contents").isArray()
                .jsonPath("$.data.contents.length()")
                .isEqualTo(2); // category1에 속한 product1, product3
        }

        @Test
        @DisplayName("정렬 옵션을 적용하여 상품 목록을 조회할 수 있다 - 낮은가격순")
        void getAllProductsByCategoryId_WithPriceAscSort_Success() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .queryParam(
                        "sort",
                        "price_asc"
                    )
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.contents").isArray()
                .jsonPath("$.data.contents[0].sellingPrice").isEqualTo(15000) // product1이 먼저
                .jsonPath("$.data.contents[1].sellingPrice").isEqualTo(48800); // product3이 다음
        }

        @Test
        @DisplayName("정렬 옵션을 적용하여 상품 목록을 조회할 수 있다 - 리뷰많은순")
        void getAllProductsByCategoryId_WithReviewDescSort_Success() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category2.id()
                    )
                    .queryParam(
                        "sort",
                        "review_desc"
                    )
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.contents").isArray()
                .jsonPath("$.data.contents.length()").isEqualTo(1); // category2에 속한 product2만
        }

        @Test
        @DisplayName("페이지네이션을 적용하여 상품 목록을 조회할 수 있다")
        void getAllProductsByCategoryId_WithPagination_Success() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .queryParam(
                        "page",
                        0
                    )
                    .queryParam(
                        "size",
                        1
                    )
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.page").isEqualTo(0)
                .jsonPath("$.data.contents").isArray()
                .jsonPath("$.data.contents.length()").isEqualTo(1); // 한 페이지에 1개만
        }

        @Test
        @DisplayName("기본값으로 상품 목록을 조회할 수 있다")
        void getAllProductsByCategoryId_WithDefaultParameters_Success() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.page").isEqualTo(0) // 기본값
                .jsonPath("$.data.contents").isArray();
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 ID로 조회 시 빈 결과를 반환한다")
        void getAllProductsByCategoryId_WithNonExistentCategoryId_EmptyResult() {
            // given
            Long nonExistentCategoryId = 999L;

            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        nonExistentCategoryId
                    )
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.contents").isArray()
                .jsonPath("$.data.contents.length()").isEqualTo(0);
        }

        @Test
        @DisplayName("잘못된 정렬 옵션으로 조회 시 실패한다")
        void getAllProductsByCategoryId_WithInvalidSort_BadRequest() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .queryParam(
                        "sort",
                        "invalid_sort"
                    )
                    .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").exists();
        }

        @Test
        @DisplayName("음수 페이지 번호로 조회 시 실패한다")
        void getAllProductsByCategoryId_WithNegativePage_BadRequest() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .queryParam(
                        "page",
                        -1
                    )
                    .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").exists();
        }

        @Test
        @DisplayName("음수 페이지 크기로 조회 시 실패한다")
        void getAllProductsByCategoryId_WithNegativeSize_BadRequest() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .queryParam(
                        "size",
                        -1
                    )
                    .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").exists();
        }

        @Test
        @DisplayName("상품 응답에 필요한 필드들이 모두 포함되어야 한다")
        void getAllProductsByCategoryId_ResponseContainsAllRequiredFields() {
            // when & then
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/products")
                    .queryParam(
                        "categoryId",
                        category1.id()
                    )
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.contents[0].id").exists()
                .jsonPath("$.data.contents[0].name").exists()
                .jsonPath("$.data.contents[0].sellingPrice").exists()
                .jsonPath("$.data.contents[0].discountPrice").exists()
                .jsonPath("$.data.contents[0].brand").exists()
                .jsonPath("$.data.contents[0].discountRate").exists()
                .jsonPath("$.data.contents[0].reviewAverageScore").exists()
                .jsonPath("$.data.contents[0].categories").exists()
                .jsonPath("$.data.contents[0].categories").isArray();
        }

    }

    @Nested
    @DisplayName("상품 기준 리뷰 목록 조회 테스트")
    class GetReviewListByProductTest {

    }
}
