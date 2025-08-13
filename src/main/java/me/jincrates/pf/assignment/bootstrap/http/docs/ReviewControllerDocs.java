package me.jincrates.pf.assignment.bootstrap.http.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.jincrates.pf.assignment.application.dto.CreateReviewRequest;
import me.jincrates.pf.assignment.application.dto.CreateReviewResponse;
import me.jincrates.pf.assignment.application.dto.UpdateReviewRequest;
import me.jincrates.pf.assignment.application.dto.UpdateReviewResponse;
import me.jincrates.pf.assignment.bootstrap.http.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "리뷰 관리", description = "리뷰 등록, 수정, 삭제 API")
public interface ReviewControllerDocs {

    @Operation(
        summary = "리뷰 등록",
        description = "상품에 대한 새로운 리뷰를 등록합니다. 상품ID, 내용, 리뷰점수(1-5점)를 설정할 수 있습니다."
    )
    ResponseEntity<BaseResponse<CreateReviewResponse>> createReview(
        @Parameter(description = "리뷰 등록 요청 데이터", required = true)
        @Valid @RequestBody CreateReviewRequest request
    );

    @Operation(
        summary = "리뷰 수정",
        description = "기존 리뷰의 내용과 리뷰점수를 수정합니다. 리뷰점수는 1~5점 범위 내에서만 설정 가능합니다."
    )
    ResponseEntity<BaseResponse<UpdateReviewResponse>> updateReview(
        @Parameter(description = "수정할 리뷰 ID", required = true, in = ParameterIn.PATH, example = "1")
        @PathVariable Long reviewId,
        @Parameter(description = "리뷰 수정 요청 데이터", required = true)
        @Valid @RequestBody UpdateReviewRequest request
    );

    @Operation(
        summary = "리뷰 삭제",
        description = "특정 리뷰를 삭제합니다."
    )
    ResponseEntity<BaseResponse<Void>> deleteReview(
        @Parameter(description = "삭제할 리뷰 ID", required = true, in = ParameterIn.PATH, example = "1")
        @PathVariable Long reviewId
    );
}
