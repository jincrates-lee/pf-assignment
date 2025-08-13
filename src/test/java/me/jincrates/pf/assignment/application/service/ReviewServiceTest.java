package me.jincrates.pf.assignment.application.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import me.jincrates.pf.assignment.application.dto.CreateReviewRequest;
import me.jincrates.pf.assignment.application.dto.CreateReviewResponse;
import me.jincrates.pf.assignment.application.dto.GetAllReviewsQuery;
import me.jincrates.pf.assignment.application.dto.ReviewResponse;
import me.jincrates.pf.assignment.application.dto.UpdateReviewRequest;
import me.jincrates.pf.assignment.application.dto.UpdateReviewResponse;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.application.repository.ReviewRepository;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Category;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.domain.model.Review;
import me.jincrates.pf.assignment.domain.vo.PageSize;
import me.jincrates.pf.assignment.domain.vo.ReviewSortType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewUseCase 테스트")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private Product product;
    private CreateReviewRequest createRequest;
    private UpdateReviewRequest updateRequest;
    private GetAllReviewsQuery query;

    @BeforeEach
    void setUp() {
        Category category = Category.builder()
            .id(1L)
            .name("반려동물")
            .depth(1)
            .parent(null)
            .build();

        product = Product.builder()
            .id(1L)
            .name("촉촉트릿 북어 80g")
            .sellingPrice(15000L)
            .discountPrice(2100L)
            .brand("촉촉트릿")
            .categories(List.of(category))
            .build();

        review = Review.builder()
            .id(1L)
            .productId(1L)
            .content("우리 강아지가 정말 좋아해요!")
            .score(5)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        createRequest = new CreateReviewRequest(
            1L,
            "우리 강아지가 정말 좋아해요!",
            5
        );

        updateRequest = new UpdateReviewRequest(
            1L,
            "수정된 리뷰 내용입니다.",
            4
        );

        query = new GetAllReviewsQuery(
            1L,
            ReviewSortType.CREATED_DESC,
            new PageSize(
                0,
                20
            )
        );
    }

    @Nested
    @DisplayName("리뷰 생성 테스트")
    class CreateReviewTest {

        @Test
        @DisplayName("정상적으로 리뷰를 생성한다")
        void shouldCreateReviewSuccessfully() {
            // given
            Review savedReview = Review.builder()
                .id(1L)
                .productId(1L)
                .content("우리 강아지가 정말 좋아해요!")
                .score(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            given(productRepository.existsById(createRequest.productId()))
                .willReturn(true);
            given(reviewRepository.save(any(Review.class)))
                .willReturn(savedReview);

            // when
            CreateReviewResponse response = reviewService.create(createRequest);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.reviewId()
            );

            verify(productRepository).existsById(createRequest.productId());
            verify(reviewRepository).save(any(Review.class));
        }

        @Test
        @DisplayName("존재하지 않는 상품에 대한 리뷰 생성 시 예외가 발생한다")
        void shouldThrowExceptionWhenProductNotFound() {
            // given
            given(productRepository.existsById(createRequest.productId()))
                .willReturn(false);

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reviewService.create(createRequest)
            );

            assertEquals(
                "상품을 찾을 수 없습니다.",
                exception.getMessage()
            );
            verify(productRepository).existsById(createRequest.productId());
            verify(
                reviewRepository,
                never()
            ).save(any(Review.class));
        }

        @Test
        @DisplayName("잘못된 점수로 리뷰 생성 시 도메인 검증에 의해 예외가 발생한다")
        void shouldThrowExceptionWhenInvalidScore() {
            // given
            CreateReviewRequest invalidRequest = new CreateReviewRequest(
                1L,
                "내용",
                6
                // 유효하지 않은 점수
            );

            given(productRepository.existsById(invalidRequest.productId()))
                .willReturn(true);

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reviewService.create(invalidRequest)
            );

            assertEquals(
                "리뷰 점수는 1점에서 5점 사이여야 합니다.",
                exception.getMessage()
            );
            verify(productRepository).existsById(invalidRequest.productId());
            verify(
                reviewRepository,
                never()
            ).save(any(Review.class));
        }

        @Test
        @DisplayName("빈 내용으로 리뷰 생성 시 도메인 검증에 의해 예외가 발생한다")
        void shouldThrowExceptionWhenContentIsBlank() {
            // given
            CreateReviewRequest invalidRequest = new CreateReviewRequest(
                1L,
                "   ",
                // 빈 내용
                5
            );

            given(productRepository.existsById(invalidRequest.productId()))
                .willReturn(true);

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reviewService.create(invalidRequest)
            );

            assertEquals(
                "리뷰 내용은 필수입니다.",
                exception.getMessage()
            );
            verify(productRepository).existsById(invalidRequest.productId());
            verify(
                reviewRepository,
                never()
            ).save(any(Review.class));
        }

        @Test
        @DisplayName("500자를 초과하는 내용으로 리뷰 생성 시 도메인 검증에 의해 예외가 발생한다")
        void shouldThrowExceptionWhenContentTooLong() {
            // given
            String longContent = "a".repeat(501); // 501자
            CreateReviewRequest invalidRequest = new CreateReviewRequest(
                1L,
                longContent,
                5
            );

            given(productRepository.existsById(invalidRequest.productId()))
                .willReturn(true);

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reviewService.create(invalidRequest)
            );

            assertEquals(
                "리뷰 내용은 500까지만 가능합니다.",
                exception.getMessage()
            );
            verify(productRepository).existsById(invalidRequest.productId());
            verify(
                reviewRepository,
                never()
            ).save(any(Review.class));
        }
    }

    @Nested
    @DisplayName("리뷰 수정 테스트")
    class UpdateReviewTest {

        @Test
        @DisplayName("정상적으로 리뷰를 수정한다")
        void shouldUpdateReviewSuccessfully() {
            // given
            Review updatedReview = Review.builder()
                .id(1L)
                .productId(1L)
                .content("수정된 리뷰 내용입니다.")
                .score(4)
                .createdAt(review.createdAt())
                .updatedAt(LocalDateTime.now())
                .build();

            given(reviewRepository.findById(updateRequest.id()))
                .willReturn(Optional.of(review));
            given(reviewRepository.update(any(Review.class)))
                .willReturn(updatedReview);

            // when
            UpdateReviewResponse response = reviewService.update(updateRequest);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.reviewId()
            );

            verify(reviewRepository).findById(updateRequest.id());
            verify(reviewRepository).update(any(Review.class));
        }

        @Test
        @DisplayName("존재하지 않는 리뷰 수정 시 예외가 발생한다")
        void shouldThrowExceptionWhenReviewNotFound() {
            // given
            given(reviewRepository.findById(updateRequest.id()))
                .willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reviewService.update(updateRequest)
            );

            assertEquals(
                "리뷰를 찾을 수 없습니다.",
                exception.getMessage()
            );
            verify(reviewRepository).findById(updateRequest.id());
            verify(
                reviewRepository,
                never()
            ).update(any(Review.class));
        }

        @Test
        @DisplayName("일부 필드만 수정할 수 있다 - content만 수정")
        void shouldUpdateOnlyContent() {
            // given
            UpdateReviewRequest partialRequest = new UpdateReviewRequest(
                1L,
                "새로운 내용",
                null
                // score는 수정하지 않음
            );

            Review updatedReview = Review.builder()
                .id(1L)
                .productId(1L)
                .content("새로운 내용")
                .score(5) // 기존 점수 유지
                .createdAt(review.createdAt())
                .updatedAt(LocalDateTime.now())
                .build();

            given(reviewRepository.findById(partialRequest.id()))
                .willReturn(Optional.of(review));
            given(reviewRepository.update(any(Review.class)))
                .willReturn(updatedReview);

            // when
            UpdateReviewResponse response = reviewService.update(partialRequest);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.reviewId()
            );

            verify(reviewRepository).findById(partialRequest.id());
            verify(reviewRepository).update(any(Review.class));
        }

        @Test
        @DisplayName("일부 필드만 수정할 수 있다 - score만 수정")
        void shouldUpdateOnlyScore() {
            // given
            UpdateReviewRequest partialRequest = new UpdateReviewRequest(
                1L,
                null,
                // content는 수정하지 않음
                3
            );

            Review updatedReview = Review.builder()
                .id(1L)
                .productId(1L)
                .content("우리 강아지가 정말 좋아해요!") // 기존 내용 유지
                .score(3)
                .createdAt(review.createdAt())
                .updatedAt(LocalDateTime.now())
                .build();

            given(reviewRepository.findById(partialRequest.id()))
                .willReturn(Optional.of(review));
            given(reviewRepository.update(any(Review.class)))
                .willReturn(updatedReview);

            // when
            UpdateReviewResponse response = reviewService.update(partialRequest);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.reviewId()
            );

            verify(reviewRepository).findById(partialRequest.id());
            verify(reviewRepository).update(any(Review.class));
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 테스트")
    class DeleteReviewTest {

        @Test
        @DisplayName("정상적으로 리뷰를 삭제한다")
        void shouldDeleteReviewSuccessfully() {
            // given
            Long reviewId = 1L;
            given(reviewRepository.existsById(reviewId))
                .willReturn(true);

            // when
            reviewService.delete(reviewId);

            // then
            verify(reviewRepository).existsById(reviewId);
            verify(reviewRepository).deleteById(reviewId);
        }

        @Test
        @DisplayName("존재하지 않는 리뷰 삭제 시 예외가 발생한다")
        void shouldThrowExceptionWhenReviewNotFound() {
            // given
            Long reviewId = 1L;
            given(reviewRepository.existsById(reviewId))
                .willReturn(false);

            // when & then
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reviewService.delete(reviewId)
            );

            assertEquals(
                "리뷰를 찾을 수 없습니다.",
                exception.getMessage()
            );
            verify(reviewRepository).existsById(reviewId);
            verify(
                reviewRepository,
                never()
            ).deleteById(reviewId);
        }
    }

    @Nested
    @DisplayName("상품별 리뷰 일괄 삭제 테스트")
    class DeleteAllByProductIdTest {

        @Test
        @DisplayName("정상적으로 상품의 모든 리뷰를 삭제한다")
        void shouldDeleteAllReviewsByProductIdSuccessfully() {
            // given
            Long productId = 1L;

            // when
            reviewService.deleteAllByProductId(productId);

            // then
            verify(reviewRepository).deleteAllByProductId(productId);
        }
    }

    @Nested
    @DisplayName("상품별 리뷰 조회 테스트")
    class GetAllReviewsByProductIdTest {

        @Test
        @DisplayName("정상적으로 상품의 리뷰 목록을 조회한다")
        void shouldGetAllReviewsByProductIdSuccessfully() {
            // given
            List<Review> reviews = List.of(review);

            given(productRepository.findById(query.productId()))
                .willReturn(Optional.of(product));
            given(reviewRepository.findAllByProductId(
                query.productId(),
                query.sort(),
                query.pageSize()
            )).willReturn(reviews);

            // when
            List<ReviewResponse> responses = reviewService.getAllReviewsByProductId(query);

            // then
            assertNotNull(responses);
            assertEquals(
                1,
                responses.size()
            );

            ReviewResponse response = responses.get(0);
            assertEquals(
                1L,
                response.id()
            );
            assertEquals(
                1L,
                response.productId()
            );
            assertEquals(
                "촉촉트릿 북어 80g",
                response.productName()
            );
            assertEquals(
                "우리 강아지가 정말 좋아해요!",
                response.content()
            );
            assertEquals(
                5,
                response.score()
            );
            assertNotNull(response.createdAt());

            verify(productRepository).findById(query.productId());
            verify(reviewRepository).findAllByProductId(
                query.productId(),
                query.sort(),
                query.pageSize()
            );
        }

        @Test
        @DisplayName("존재하지 않는 상품의 리뷰 조회 시 빈 목록을 반환한다")
        void shouldReturnEmptyListWhenProductNotFound() {
            // given
            given(productRepository.findById(query.productId()))
                .willReturn(Optional.empty());

            // when
            List<ReviewResponse> responses = reviewService.getAllReviewsByProductId(query);

            // then
            assertNotNull(responses);
            assertEquals(
                0,
                responses.size()
            );

            verify(productRepository).findById(query.productId());
            verify(
                reviewRepository,
                never()
            ).findAllByProductId(
                any(),
                any(),
                any()
            );
        }

        @Test
        @DisplayName("상품은 존재하지만 리뷰가 없는 경우 빈 목록을 반환한다")
        void shouldReturnEmptyListWhenNoReviews() {
            // given
            given(productRepository.findById(query.productId()))
                .willReturn(Optional.of(product));
            given(reviewRepository.findAllByProductId(
                query.productId(),
                query.sort(),
                query.pageSize()
            )).willReturn(Collections.emptyList());

            // when
            List<ReviewResponse> responses = reviewService.getAllReviewsByProductId(query);

            // then
            assertNotNull(responses);
            assertEquals(
                0,
                responses.size()
            );

            verify(productRepository).findById(query.productId());
            verify(reviewRepository).findAllByProductId(
                query.productId(),
                query.sort(),
                query.pageSize()
            );
        }

        @Test
        @DisplayName("여러 리뷰가 있는 경우 모두 조회한다")
        void shouldGetAllReviewsWhenMultipleReviews() {
            // given
            Review review2 = Review.builder()
                .id(2L)
                .productId(1L)
                .content("두 번째 리뷰입니다.")
                .score(4)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .build();

            List<Review> reviews = List.of(
                review,
                review2
            );

            given(productRepository.findById(query.productId()))
                .willReturn(Optional.of(product));
            given(reviewRepository.findAllByProductId(
                query.productId(),
                query.sort(),
                query.pageSize()
            )).willReturn(reviews);

            // when
            List<ReviewResponse> responses = reviewService.getAllReviewsByProductId(query);

            // then
            assertNotNull(responses);
            assertEquals(
                2,
                responses.size()
            );

            // 첫 번째 리뷰 검증
            ReviewResponse firstResponse = responses.get(0);
            assertEquals(
                1L,
                firstResponse.id()
            );
            assertEquals(
                "우리 강아지가 정말 좋아해요!",
                firstResponse.content()
            );
            assertEquals(
                5,
                firstResponse.score()
            );

            // 두 번째 리뷰 검증
            ReviewResponse secondResponse = responses.get(1);
            assertEquals(
                2L,
                secondResponse.id()
            );
            assertEquals(
                "두 번째 리뷰입니다.",
                secondResponse.content()
            );
            assertEquals(
                4,
                secondResponse.score()
            );

            verify(productRepository).findById(query.productId());
            verify(reviewRepository).findAllByProductId(
                query.productId(),
                query.sort(),
                query.pageSize()
            );
        }
    }
}
