package me.jincrates.pf.assignment.bootstrap.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
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
                .sellingPrice(BigDecimal.valueOf(30000))
                .discountPrice(BigDecimal.valueOf(25000))
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
                .sellingPrice(BigDecimal.valueOf(30000))
                .discountPrice(BigDecimal.valueOf(25000))
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
}
