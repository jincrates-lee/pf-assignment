package me.jincrates.pf.assignment.domain.vo;

import lombok.Getter;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public enum ProductSortType {
    /**
     * 낮은 가격 순
     */
    SELLING_PRICE_ASC(
        "price_asc",
        "sellingPrice",
        Direction.ASC
    ),
    /**
     * 리뷰가 많은 순
     */
    REVIEW_COUNT_DESC(
        "review_desc",
        "reviewCount",
        Direction.DESC
    ),
    ;

    @Getter
    private final String value;
    private final String field;
    private final Direction direction;

    ProductSortType(
        final String value,
        final String field,
        final Direction direction
    ) {
        this.value = value;
        this.field = field;
        this.direction = direction;
    }

    public static ProductSortType fromValue(String value) {
        for (ProductSortType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new BusinessException("지원하지 않는 정렬 기준입니다: " + value);
    }

    public Sort toSort() {
        return Sort.by(
            direction,
            field
        );
    }
}
