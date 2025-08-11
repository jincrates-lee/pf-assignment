package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Schema(description = "리뷰 수정 요청")
public record UpdateReviewRequest(
    @Schema(description = "리뷰 ID", hidden = true)
    @With
    Long id,
    @Schema(description = "리뷰 내용", example = "수정된 리뷰 내용입니다.")
    @Length(max = 500, message = "리뷰 내용은 500자까지만 가능합니다.")
    String content,
    @Schema(description = "리뷰 점수 (1-5점)", example = "4")
    @Range(min = 1, max = 5, message = "리뷰 점수는 1점에서 5점 사이여야 합니다.")
    Integer score
) {

}
