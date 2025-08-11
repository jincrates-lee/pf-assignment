package me.jincrates.pf.assignment.application.dto;

import lombok.Builder;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.vo.PageSize;
import me.jincrates.pf.assignment.domain.vo.ProductSortType;

@Builder
public record GetAllProductsQuery(
    Long categoryId,
    ProductSortType sort,
    PageSize pageSize
) {

    public GetAllProductsQuery {
        if (categoryId == null) {
            throw new BusinessException("카테고리 ID는 필수입니다.");
        }
        if (sort == null) {
            throw new BusinessException("정렬 기준은 필수입니다.");
        }
        if (pageSize == null) {
            throw new BusinessException("페이징 정보는 필수입니다.");
        }
    }
}
