package me.jincrates.pf.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;
import lombok.With;

public record UpdateProductRequest(
    @With
    @JsonIgnoreProperties(ignoreUnknown = true)
    Long id,
    @NotBlank(message = "상품 이름은 필수입니다.")
    String name,
    @Positive(message = "상품 판매가는 0원보다 커야합니다.")
    BigDecimal sellingPrice,
    @PositiveOrZero(message = "상품 할인가는 0원 이상이여야 합니다.")
    BigDecimal discountPrice,
    @NotBlank(message = "상품 브랜드는 필수입니다.")
    String brand,
    @NotEmpty(message = "상품 카테고리 목록은 필수입니다.")
    Set<Long> categoryIds
) {

}
