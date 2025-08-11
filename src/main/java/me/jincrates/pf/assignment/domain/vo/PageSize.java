package me.jincrates.pf.assignment.domain.vo;

import me.jincrates.pf.assignment.domain.exception.BusinessException;

public record PageSize(
    int page,
    int size
) {

    public PageSize {
        if (page < 0) {
            throw new BusinessException("페이지 번호는 0 이상이여야 합니다.");
        }
        if (size <= 0) {
            throw new BusinessException("페이지 사이즈는 0보다 커야합니다.");
        }
    }
}
