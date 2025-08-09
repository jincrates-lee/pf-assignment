package me.jincrates.pf.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;
import lombok.With;
import me.jincrates.pf.assignment.domain.exception.BusinessException;

public record UpdateProductRequest(
    @With
    @JsonIgnoreProperties(ignoreUnknown = true)
    Long id,
    String name,
    @Positive(message = "상품 판매가는 0원보다 커야합니다.")
    BigDecimal sellingPrice,
    @PositiveOrZero(message = "상품 할인가는 0원 이상이여야 합니다.")
    BigDecimal discountPrice,
    String brand,
    Set<Long> categoryIds
) {

    public UpdateProductRequest {
        if (name != null && name.isBlank()) {
            throw new BusinessException("상품 이름은 공백일 수 없습니다.");
        }
        if (sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("상품 판매가는 0원보다 커야합니다.");
        }
        if (discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("상품 할인가는 0원 이상이여야 합니다.");
        }
        if (categoryIds != null && categoryIds.isEmpty()) {
            throw new BusinessException("상품 카테고리는 필수입니다.");
        }
    }
}
