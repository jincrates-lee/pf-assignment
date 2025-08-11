package me.jincrates.pf.assignment.application.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import me.jincrates.pf.assignment.domain.model.Review;
import me.jincrates.pf.assignment.domain.vo.PageSize;
import me.jincrates.pf.assignment.domain.vo.ReviewSortType;

public interface ReviewRepository {

    Review save(Review review);

    Review update(Review newReview);

    Optional<Review> findById(Long reviewId);

    boolean existsById(Long reviewId);

    void deleteById(Long reviewId);

    void deleteAllByProductId(Long productId);

    List<Review> findAllByProductId(
        Long productId,
        ReviewSortType sort,
        PageSize pageSize
    );

    List<Review> findAllByProductIdIn(List<Long> productIds);

    Map<Long, Double> findAverageScoreByProductIdIn(List<Long> productIds);
}
