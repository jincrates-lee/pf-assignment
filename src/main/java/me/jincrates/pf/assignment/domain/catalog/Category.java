package me.jincrates.pf.assignment.domain.catalog;

import lombok.Builder;

@Builder
public record Category(
    Long id,
    String name,
    Integer depth,
    Category parent
) {

    public Category {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리 이름은 필수입니다.");
        }
        if (depth == null) {
            throw new IllegalArgumentException("카테고리 단계는 필수입니다.");
        }
        if (depth <= 0) {
            throw new IllegalArgumentException("카테고리 단계는 0보다 커야합니다.");
        }
    }
}
