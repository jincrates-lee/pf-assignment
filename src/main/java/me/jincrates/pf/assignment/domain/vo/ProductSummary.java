package me.jincrates.pf.assignment.domain.vo;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import me.jincrates.pf.assignment.domain.model.Category;

@Builder
public record ProductSummary(
    Long id,
    String name,
    BigDecimal sellingPrice,
    BigDecimal discountPrice,
    String brand,
    long discountRate,
    long reviewAverageScore,
    List<Category> categories
) {

}
