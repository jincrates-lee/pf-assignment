package me.jincrates.pf.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.With;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record UpdateReviewRequest(
    @With
    @JsonIgnoreProperties(ignoreUnknown = true)
    Long id,
    @Length(max = 500, message = "리뷰 내용은 500자까지만 가능합니다.")
    String content,
    @Range(min = 1, max = 5, message = "리뷰 점수는 1점에서 5점 사이여야 합니다.")
    Integer score
) {

}
