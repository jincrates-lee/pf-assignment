package me.jincrates.pf.assignment.domain.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import me.jincrates.pf.assignment.domain.exception.BusinessException;

@Builder
public record Product(
    Long id,
    String name,
    BigDecimal sellingPrice,
    BigDecimal discountPrice,
    String brand,
    List<Category> categories
) {

    public Product {
        if (name == null || name.isBlank()) {
            throw new BusinessException("상품 이름은 필수입니다.");
        }
        if (sellingPrice == null || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("상품 판매가는 0원보다 커야합니다.");
        }
        if (discountPrice == null || discountPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("상품 할인가는 0원 이상이여야 합니다.");
        }
        if (sellingPrice.compareTo(discountPrice) <= 0) {
            throw new BusinessException("상품 할인가는 상품 판매가보다 클 수 없습니다.");
        }
        if (brand == null || brand.isBlank()) {
            throw new BusinessException("상품 브랜드는 필수입니다.");
        }
        if (categories == null || categories.isEmpty()) {
            throw new BusinessException("상품 카테고리 목록은 필수입니다.");
        }
    }
}
