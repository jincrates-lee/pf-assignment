package me.jincrates.pf.assignment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record CreateReviewRequest(
    @NotNull(message = "상품 ID는 필수입니다.")
    Long productId,
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Length(max = 500, message = "리뷰 내용은 500자까지만 가능합니다.")
    String content,
    @NotNull(message = "리뷰 점수는 필수입니다.")
    @Range(min = 1, max = 5, message = "리뷰 점수는 1점에서 5점 사이여야 합니다.")
    Integer score
) {

}
