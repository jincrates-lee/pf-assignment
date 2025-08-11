package me.jincrates.pf.assignment.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.repository.ReviewRepository;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Review;
import me.jincrates.pf.assignment.domain.vo.ProductAverageScore;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ReviewJpaEntity;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper.ReviewJpaMapper;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository.ReviewJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReviewRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository repository;

    @Override
    public Review save(final Review review) {
        ReviewJpaEntity entity = ReviewJpaMapper.toEntity(review);
        ReviewJpaEntity saved = repository.save(entity);
        return ReviewJpaMapper.toDomain(saved);
    }

    @Override
    public Review update(final Review newReview) {
        if (newReview.id() == null) {
            throw new BusinessException("리뷰 ID가 잘못되었습니다.");
        }
        return save(newReview);
    }

    @Override
    public Optional<Review> findById(final Long reviewId) {
        return repository.findById(reviewId)
            .map(ReviewJpaMapper::toDomain);
    }

    @Override
    public boolean existsById(final Long reviewId) {
        return repository.existsById(reviewId);
    }

    @Override
    public void deleteById(final Long reviewId) {
        repository.deleteById(reviewId);
    }

    @Override
    public void deleteAllByProductId(final Long productId) {
        repository.deleteAllByProductId(productId);
    }

    @Override
    public List<Review> findAllByProductIdIn(final List<Long> productIds) {
        return repository.findAllByProductIdIn(productIds).stream()
            .map(ReviewJpaMapper::toDomain)
            .toList();
    }

    /**
     * 상품 ID별 리뷰 평균 점수 Map
     */
    @Override
    public Map<Long, Long> findAverageScoreByProductIdIn(final List<Long> productIds) {
        List<ProductAverageScore> result = repository.findAverageScoreByProductIdIn(productIds);
        return result.stream()
            .collect(Collectors.toMap(
                    ProductAverageScore::productId,
                    it -> BigDecimal.valueOf(it.averageScore())
                        .setScale(
                            2,
                            RoundingMode.HALF_UP
                        )
                        .longValue()
                )
            );
    }
}
