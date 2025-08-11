package me.jincrates.pf.assignment.application.dto;

import lombok.Builder;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.vo.PageSize;

@Builder
public record GetAllReviewsQuery(
    Long productId,
    String sort,
    PageSize pageSize
) {

    private static final String SORT_BY_CREATED_AT = "createdAt_desc";

    public GetAllReviewsQuery {
        if (productId == null) {
            throw new BusinessException("상품 ID는 필수입니다.");
        }
        if (sort == null || sort.isBlank()) {
            throw new BusinessException("정렬 기준은 필수입니다.");
        }
        if (pageSize == null) {
            throw new BusinessException("페이징 정보는 필수입니다.");
        }
        if (SORT_BY_CREATED_AT.equals(sort)) {
            throw new BusinessException("정렬 기준이 잘못되었습니다.");
        }
    }
}
