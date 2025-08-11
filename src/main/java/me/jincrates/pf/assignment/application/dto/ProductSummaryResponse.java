package me.jincrates.pf.assignment.application.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ProductSummaryResponse(
    Long id,
    String name,
    Long sellingPrice,
    Long discountPrice,
    String brand,
    Long discountRate,
    Double reviewAverageScore,
    List<CategoryResponse> categories
) {

    @Builder
    public record CategoryResponse(
        Long id,
        String name,
        Integer depth,
        CategoryResponse parent
    ) {

    }
}
