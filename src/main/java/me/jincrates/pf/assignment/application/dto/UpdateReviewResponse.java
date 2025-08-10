package me.jincrates.pf.assignment.application.dto;

import lombok.Builder;

@Builder
public record UpdateReviewResponse(
    Long reviewId
) {

}
