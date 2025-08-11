package me.jincrates.pf.assignment.application.dto;

import java.util.Comparator;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductSummaryResponse(
    Long id,
    String name,
    long sellingPrice,
    long discountPrice,
    String brand,
    long discountRate,
    long reviewAverageScore,
    List<CategoryResponse> categories
) {

    public ProductSummaryResponse {
        if (categories != null) {
            categories = categories.stream()
                .sorted(Comparator.comparing(CategoryResponse::depth)) // depth 오름차순
                .toList();
        }
    }

    @Builder
    public record CategoryResponse(
        Long id,
        String name,
        Integer depth,
        CategoryResponse parent
    ) {

    }
}
