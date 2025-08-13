package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Schema(description = "리뷰 생성 요청")
public record CreateReviewRequest(
    @Schema(description = "상품 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "상품 ID는 필수입니다.")
    Long productId,
    @Schema(description = "리뷰 내용", example = "우리 강아지가 정말 좋아해요!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Length(max = 500, message = "리뷰 내용은 500자까지만 가능합니다.")
    String content,
    @Schema(description = "리뷰 점수 (1~5점)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "리뷰 점수는 필수입니다.")
    @Range(min = 1, max = 5, message = "리뷰 점수는 1점에서 5점 사이여야 합니다.")
    Integer score
) {

}
