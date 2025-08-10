package me.jincrates.pf.assignment.application.repository;

import java.util.Optional;
import me.jincrates.pf.assignment.domain.model.Review;

public interface ReviewRepository {

    Review save(Review review);

    Review update(Review newReview);

    Optional<Review> findById(Long reviewId);

    boolean existsById(Long reviewId);

    void deleteById(Long reviewId);

    void deleteAllByProductId(Long productId);
}
