package me.jincrates.pf.assignment.application.service;

import static me.jincrates.pf.assignment.shared.util.ValueUtil.defaultIfNull;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.ReviewUseCase;
import me.jincrates.pf.assignment.application.dto.CreateReviewRequest;
import me.jincrates.pf.assignment.application.dto.CreateReviewResponse;
import me.jincrates.pf.assignment.application.dto.GetAllReviewsQuery;
import me.jincrates.pf.assignment.application.dto.ReviewResponse;
import me.jincrates.pf.assignment.application.dto.UpdateReviewRequest;
import me.jincrates.pf.assignment.application.dto.UpdateReviewResponse;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.application.repository.ReviewRepository;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.domain.model.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ReviewService implements ReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CreateReviewResponse create(final CreateReviewRequest request) {
        if (!productRepository.existsById(request.productId())) {
            throw new BusinessException(
                "상품을 찾을 수 없습니다.",
                Map.of(
                    "productId",
                    request.productId()
                )
            );
        }

        Review review = Review.builder()
            .productId(request.productId())
            .content(request.content())
            .score(request.score())
            .build();

        Review saved = reviewRepository.save(review);

        return CreateReviewResponse.builder()
            .reviewId(saved.id())
            .build();
    }

    @Override
    @Transactional
    public UpdateReviewResponse update(final UpdateReviewRequest request) {
        Review existing = reviewRepository.findById(request.id())
            .orElseThrow(() -> new BusinessException(
                "리뷰를 찾을 수 없습니다.",
                Map.of(
                    "reviewId",
                    request.id()
                )
            ));

        Review review = Review.builder()
            .id(existing.id())
            .productId(existing.productId())
            .content(defaultIfNull(
                request.content(),
                existing.content()
            ))
            .score(defaultIfNull(
                request.score(),
                existing.score()
            ))
            .createdAt(existing.createdAt())
            .updatedAt(existing.updatedAt())  // 기존 값을 넣어도 jpa auditing에 의해 새로운 날짜가 입력됨
            .build();

        Review updated = reviewRepository.update(review);

        return UpdateReviewResponse.builder()
            .reviewId(updated.id())
            .build();
    }

    @Override
    @Transactional
    public void delete(final Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new BusinessException(
                "리뷰를 찾을 수 없습니다.",
                Map.of(
                    "reviewId",
                    reviewId
                )
            );
        }

        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional
    public void deleteAllByProductId(final Long productId) {
        reviewRepository.deleteAllByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviewsByProductId(final GetAllReviewsQuery query) {
        Product product = productRepository.findById(query.productId())
            .orElseThrow(() -> new BusinessException(
                "상품을 찾을 수 없습니다.",
                Map.of(
                    "productId",
                    query.productId()
                )
            ));

        List<Review> reviews = reviewRepository.findAllByProductId(
            query.productId(),
            query.sort(),
            query.pageSize()
        );

        return reviews.stream()
            .map(it -> ReviewResponse.builder()
                .id(it.id())
                .productId(it.productId())
                .productName(product.name())
                .content(it.content())
                .score(it.score())
                .createdAt(it.createdAt())
                .build())
            .toList();
    }
}
