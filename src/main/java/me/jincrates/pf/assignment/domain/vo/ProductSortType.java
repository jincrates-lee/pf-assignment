package me.jincrates.pf.assignment.domain.vo;

import lombok.Getter;
import me.jincrates.pf.assignment.domain.exception.BusinessException;

@Getter
public enum ProductSortType {
    /**
     * 낮은 가격 순
     */
    SELLING_PRICE_ASC("price_asc"),
    /**
     * 리뷰가 많은 순
     */
    REVIEW_COUNT_DESC("review_desc"),
    ;

    private final String value;

    ProductSortType(final String value) {
        this.value = value;
    }

    public static ProductSortType fromValue(String value) {
        for (ProductSortType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new BusinessException("지원하지 않는 정렬 기준입니다: " + value);
    }
}
