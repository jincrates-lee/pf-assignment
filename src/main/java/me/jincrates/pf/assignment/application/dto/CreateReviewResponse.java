package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "리뷰 생성 응답")
@Builder
public record CreateReviewResponse(
    @Schema(description = "생성된 리뷰 ID", example = "1")
    Long reviewId
) {

}
