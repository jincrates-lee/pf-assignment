package me.jincrates.pf.assignment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;

public record CreateProductRequest(
    @NotBlank(message = "상품 이름은 필수입니다.")
    String name,
    @NotNull(message = "상품 판매가는 필수입니다.")
    @Positive(message = "상품 판매가는 0원보다 커야합니다.")
    BigDecimal sellingPrice,
    @NotNull(message = "상품 할인가는 필수입니다.")
    @PositiveOrZero(message = "상품 할인가는 0원 이상이여야 합니다.")
    BigDecimal discountPrice,
    @NotBlank(message = "상품 브랜드는 필수입니다.")
    String brand,
    @NotEmpty(message = "상품 카테고리 목록은 필수입니다.")
    Set<Long> categoryIds
) {

}
