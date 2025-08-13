package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "리뷰 수정 응답")
@Builder
public record UpdateReviewResponse(
    @Schema(description = "수정된 리뷰 ID", example = "1")
    Long reviewId
) {

}
