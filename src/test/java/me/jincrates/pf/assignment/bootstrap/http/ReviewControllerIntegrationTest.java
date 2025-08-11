package me.jincrates.pf.assignment.bootstrap.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.jincrates.pf.assignment.IntegrationTestSupport;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.application.repository.ReviewRepository;
import me.jincrates.pf.assignment.domain.model.Category;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.domain.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Tag("integration")
@DisplayName("리뷰 API 통합 테스트")
class ReviewControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = categoryRepository.save(Category.builder()
            .name("사료")
            .depth(1)
            .build());
        product = productRepository.save(Product.builder()
            .name("기존 상품")
            .sellingPrice(30000L)
            .discountPrice(25000L)
            .brand("기존 브랜드")
            .categories(List.of(category))
            .build());
    }

    @Nested
    @DisplayName("리뷰 생성 테스트")
    class CreateReviewTest {

        @Test
        @DisplayName("유효한 리뷰 정보로 리뷰를 생성할 수 있다")
        void createReview_WithValidData_Success() {
            // given
            String requestBody = String.format(
                """
                    {
                        "productId": %d,
                        "content": "정말 좋은 상품이에요!",
                        "score": 5
                    }
                    """,
                product.id()
            );

            // when
            webTestClient.post()
                .uri("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(null)
                .jsonPath("$.data.reviewId").isNumber();
        }

        @Test
        @DisplayName("상품 ID가 null일 때 생성에 실패한다")
        void createReview_WithNullProductId_BadRequest() {
            // given
            String requestBody = """
                {
                    "productId": null,
                    "content": "정말 좋은 상품이에요!",
                    "score": 5
                }
                """;

            // when
            webTestClient.post()
                .uri("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상품 ID는 필수입니다. 입력된 값: [productId=null]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("리뷰 내용이 공백일 때 생성에 실패한다")
        void createReview_WithBlankContent_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "productId": %d,
                        "content": "",
                        "score": 5
                    }
                    """,
                product.id()
            );

            // when
            webTestClient.post()
                .uri("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("리뷰 내용은 필수입니다. 입력된 값: [content=]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("리뷰 내용이 500자를 초과할 때 생성에 실패한다")
        void createReview_WithLongContent_BadRequest() {
            // given
            String longContent = "a".repeat(501);
            String requestBody = String.format(
                """
                    {
                        "productId": %d,
                        "content": "%s",
                        "score": 5
                    }
                    """,
                product.id(),
                longContent
            );

            // when
            webTestClient.post()
                .uri("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo(String.format(
                    "리뷰 내용은 500자까지만 가능합니다. 입력된 값: [content=%s]",
                    longContent
                ))
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("리뷰 점수가 null일 때 생성에 실패한다")
        void createReview_WithNullScore_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "productId": %d,
                        "content": "좋은 상품입니다.",
                        "score": null
                    }
                    """,
                product.id()
            );

            // when
            webTestClient.post()
                .uri("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("리뷰 점수는 필수입니다. 입력된 값: [score=null]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("리뷰 점수가 1미만일 때 생성에 실패한다")
        void createReview_WithScoreLessThan1_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "productId": %d,
                        "content": "좋은 상품입니다.",
                        "score": 0
                    }
                    """,
                product.id()
            );

            // when
            webTestClient.post()
                .uri("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("리뷰 점수는 1점에서 5점 사이여야 합니다. 입력된 값: [score=0]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("리뷰 점수가 5초과일 때 생성에 실패한다")
        void createReview_WithScoreGreaterThan5_BadRequest() {
            // given
            String requestBody = String.format(
                """
                    {
                        "productId": %d,
                        "content": "좋은 상품입니다.",
                        "score": 6
                    }
                    """,
                product.id()
            );

            // when
            webTestClient.post()
                .uri("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("리뷰 점수는 1점에서 5점 사이여야 합니다. 입력된 값: [score=6]")
                .jsonPath("$.data").isEmpty();
        }
    }

    @Nested
    @DisplayName("리뷰 수정 테스트")
    class UpdateReviewTest {

        private Review savedReview;

        @BeforeEach
        void setUp() {
            savedReview = reviewRepository.save(Review.builder()
                .productId(product.id())
                .content("기존 리뷰 내용")
                .score(4)
                .build());
        }

        @Test
        @DisplayName("유효한 리뷰 정보로 리뷰를 수정할 수 있다")
        void updateReview_WithValidData_Success() {
            // given
            String requestBody = """
                {
                    "content": "수정된 리뷰 내용입니다.",
                    "score": 5
                }
                """;

            // when
            webTestClient.patch()
                .uri(
                    "/api/reviews/{reviewId}",
                    savedReview.id()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(null)
                .jsonPath("$.data.reviewId").isEqualTo(savedReview.id());
        }

        @Test
        @DisplayName("존재하지 않는 리뷰 수정 시 실패한다")
        void updateReview_WithNonExistentReview_NotFound() {
            // given
            Long nonExistentReviewId = 999L;
            String requestBody = """
                {
                    "content": "수정된 리뷰 내용입니다.",
                    "score": 5
                }
                """;

            // when
            webTestClient.patch()
                .uri(
                    "/api/reviews/{reviewId}",
                    nonExistentReviewId
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("리뷰를 찾을 수 없습니다.")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("리뷰 내용이 500자를 초과할 때 수정에 실패한다")
        void updateReview_WithLongContent_BadRequest() {
            // given
            String longContent = "a".repeat(501);
            String requestBody = String.format(
                """
                    {
                        "content": "%s",
                        "score": 5
                    }
                    """,
                longContent
            );

            // when
            webTestClient.patch()
                .uri(
                    "/api/reviews/{reviewId}",
                    savedReview.id()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo(String.format(
                    "리뷰 내용은 500자까지만 가능합니다. 입력된 값: [content=%s]",
                    longContent
                ))
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("리뷰 점수가 범위를 벗어날 때 수정에 실패한다")
        void updateReview_WithInvalidScore_BadRequest() {
            // given
            String requestBody = """
                {
                    "content": "수정된 내용",
                    "score": 0
                }
                """;

            // when
            webTestClient.patch()
                .uri(
                    "/api/reviews/{reviewId}",
                    savedReview.id()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("리뷰 점수는 1점에서 5점 사이여야 합니다. 입력된 값: [score=0]")
                .jsonPath("$.data").isEmpty();
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 테스트")
    class DeleteReviewTest {

        private Review savedReview;

        @BeforeEach
        void setUp() {
            savedReview = reviewRepository.save(Review.builder()
                .productId(product.id())
                .content("삭제할 리뷰 내용")
                .score(3)
                .build());
        }

        @Test
        @DisplayName("존재하는 리뷰를 삭제할 수 있다")
        void deleteReview_WithExistingReview_Success() {
            // when & then
            webTestClient.delete()
                .uri(
                    "/api/reviews/{reviewId}",
                    savedReview.id()
                )
                .exchange()
                .expectStatus().isNoContent();

            // 실제로 삭제되었는지 확인
            assertThat(reviewRepository.findById(savedReview.id())).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 리뷰 삭제 시 실패한다")
        void deleteReview_WithNonExistentReview_NotFound() {
            // given
            Long nonExistentReviewId = 999L;

            // when
            webTestClient.delete()
                .uri(
                    "/api/reviews/{reviewId}",
                    nonExistentReviewId
                )
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("리뷰를 찾을 수 없습니다.")
                .jsonPath("$.data").isEmpty();
        }
    }
}
