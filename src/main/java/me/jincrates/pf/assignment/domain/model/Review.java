package me.jincrates.pf.assignment.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import me.jincrates.pf.assignment.domain.exception.BusinessException;

@Builder
public record Review(
    Long id,
    Long productId,
    String content,
    Integer score,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public Review {
        if (productId == null) {
            throw new BusinessException("상품 ID는 필수입니다.");
        }
        if (content == null || content.isBlank()) {
            throw new BusinessException("리뷰 내용은 필수입니다.");
        }
        if (content.length() > 500) {
            throw new BusinessException("리뷰 내용은 500까지만 가능합니다.");
        }
        if (score == null) {
            throw new BusinessException("리뷰 점수는 필수입니다.");
        }
        if (score < 1 || score > 5) {
            throw new BusinessException("리뷰 점수는 1점에서 5점 사이여야 합니다.");
        }
    }
}
