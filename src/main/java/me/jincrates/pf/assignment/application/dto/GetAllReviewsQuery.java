package me.jincrates.pf.assignment.application.dto;

import lombok.Builder;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.vo.PageSize;
import me.jincrates.pf.assignment.domain.vo.ReviewSortType;

@Builder
public record GetAllReviewsQuery(
    Long productId,
    ReviewSortType sort,
    PageSize pageSize
) {

    public GetAllReviewsQuery {
        if (productId == null) {
            throw new BusinessException("상품 ID는 필수입니다.");
        }
        if (sort == null) {
            throw new BusinessException("정렬 기준은 필수입니다.");
        }
        if (pageSize == null) {
            throw new BusinessException("페이징 정보는 필수입니다.");
        }
    }
}
