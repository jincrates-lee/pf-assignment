package me.jincrates.pf.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Set;
import lombok.With;

public record UpdateProductRequest(
    @With
    @JsonIgnoreProperties(ignoreUnknown = true)
    Long id,
    String name,
    @Positive(message = "상품 판매가는 0원보다 커야합니다.")
    Long sellingPrice,
    @PositiveOrZero(message = "상품 할인가는 0원 이상이여야 합니다.")
    Long discountPrice,
    String brand,
    Set<Long> categoryIds
) {

}
