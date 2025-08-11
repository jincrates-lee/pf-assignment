package me.jincrates.pf.assignment.application.dto;

import java.util.List;
import lombok.Builder;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.vo.PageSize;

@Builder
public record GetAllProductsQuery(
    Long categoryId,
    String sort,  // price_asc, review_desc
    PageSize pageSize
) {

    private static final String SORT_BY_PRICE = "price_asc";
    private static final String SORT_BY_REVIEW = "review_desc";

    public GetAllProductsQuery {
        if (categoryId == null) {
            throw new BusinessException("카테고리 ID는 필수입니다.");
        }
        if (sort == null || sort.isBlank()) {
            throw new BusinessException("정렬 기준은 필수입니다.");
        }
        if (pageSize == null) {
            throw new BusinessException("페이징 정보는 필수입니다.");
        }
        if (!List.of(
            SORT_BY_PRICE,
            SORT_BY_REVIEW
        ).contains(sort)) {
            throw new BusinessException("정렬 기준이 잘못되었습니다.");
        }

    }
}
