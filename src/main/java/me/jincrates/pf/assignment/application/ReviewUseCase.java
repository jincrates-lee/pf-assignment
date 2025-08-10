package me.jincrates.pf.assignment.application;

import me.jincrates.pf.assignment.application.dto.CreateReviewRequest;
import me.jincrates.pf.assignment.application.dto.CreateReviewResponse;
import me.jincrates.pf.assignment.application.dto.UpdateReviewRequest;
import me.jincrates.pf.assignment.application.dto.UpdateReviewResponse;

public interface ReviewUseCase {

    CreateReviewResponse create(CreateReviewRequest request);

    UpdateReviewResponse update(UpdateReviewRequest request);

    void delete(Long reviewId);

    void deleteAllByProductId(Long productId);
}
