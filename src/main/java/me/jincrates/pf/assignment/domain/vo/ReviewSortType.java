package me.jincrates.pf.assignment.domain.vo;

import lombok.Getter;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public enum ReviewSortType {
    CREATED_DESC(
        "created_desc",
        "createdAt",
        Direction.DESC
    ),
    CREATED_ASC(
        "created_asc",
        "createdAt",
        Direction.ASC
    ),
    ;

    @Getter
    private final String value;
    private final String field;
    private final Sort.Direction direction;

    ReviewSortType(
        final String value,
        final String field,
        final Direction direction
    ) {
        this.value = value;
        this.field = field;
        this.direction = direction;
    }

    public static ReviewSortType fromValue(String value) {
        for (ReviewSortType type : values()) {
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
