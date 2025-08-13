package me.jincrates.pf.assignment.bootstrap.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.ReviewUseCase;
import me.jincrates.pf.assignment.application.dto.CreateReviewRequest;
import me.jincrates.pf.assignment.application.dto.CreateReviewResponse;
import me.jincrates.pf.assignment.application.dto.UpdateReviewRequest;
import me.jincrates.pf.assignment.application.dto.UpdateReviewResponse;
import me.jincrates.pf.assignment.bootstrap.http.docs.ReviewControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
class ReviewController extends BaseController implements ReviewControllerDocs {

    private final ReviewUseCase useCase;

    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<CreateReviewResponse>> createReview(
        @Valid @RequestBody CreateReviewRequest request
    ) {
        return created(useCase.create(request));
    }

    @Override
    @PatchMapping("/{reviewId}")
    public ResponseEntity<BaseResponse<UpdateReviewResponse>> updateReview(
        @PathVariable Long reviewId,
        @Valid @RequestBody UpdateReviewRequest request
    ) {
        return ok(useCase.update(request.withId(reviewId)));
    }

    @Override
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<BaseResponse<Void>> deleteReview(
        @PathVariable Long reviewId
    ) {
        useCase.delete(reviewId);
        return ok();
    }
}
