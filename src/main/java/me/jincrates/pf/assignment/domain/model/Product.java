package me.jincrates.pf.assignment.domain.model;

import java.util.List;
import lombok.Builder;
import me.jincrates.pf.assignment.domain.exception.BusinessException;

@Builder
public record Product(
    Long id,
    String name,
    Long sellingPrice,
    Long discountPrice,
    String brand,
    List<Category> categories
) {

    public Product {
        if (name == null || name.isBlank()) {
            throw new BusinessException("상품 이름은 필수입니다.");
        }
        if (sellingPrice == null || sellingPrice <= 0) {
            throw new BusinessException("상품 판매가는 0원보다 커야합니다.");
        }
        if (discountPrice == null || discountPrice < 0) {
            throw new BusinessException("상품 할인가는 0원 이상이여야 합니다.");
        }
        if (sellingPrice < discountPrice) {
            throw new BusinessException("상품 할인가는 판매가보다 작아야 합니다.");
        }
        if (brand == null || brand.isBlank()) {
            throw new BusinessException("상품 브랜드는 필수입니다.");
        }
        if (categories == null || categories.isEmpty()) {
            throw new BusinessException("상품 카테고리 목록은 필수입니다.");
        }
    }

    /**
     * 할인율 = 할인가 / 판매가 * 100
     *
     * @return 할인율(소수점 첫번쨰 자리에서 반올림)
     */
    public long calculateDiscountRate() {
        return Math.round(((double) discountPrice / sellingPrice) * 100.0);
    }
}
