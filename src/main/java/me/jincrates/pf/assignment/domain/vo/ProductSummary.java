package me.jincrates.pf.assignment.domain.vo;

import java.util.List;
import lombok.Builder;
import me.jincrates.pf.assignment.domain.model.Category;

@Builder
public record ProductSummary(
    Long id,
    String name,
    Long sellingPrice,
    Long discountPrice,
    String brand,
    Long discountRate,
    Double reviewAverageScore,
    List<Category> categories
) {

}
