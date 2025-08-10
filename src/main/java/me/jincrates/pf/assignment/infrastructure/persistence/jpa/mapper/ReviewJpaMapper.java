package me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper;

import me.jincrates.pf.assignment.domain.model.Review;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ReviewJpaEntity;

public class ReviewJpaMapper {

    public static ReviewJpaEntity toEntity(final Review review) {
        return ReviewJpaEntity.builder()
            .id(review.id())
            .productId(review.productId())
            .content(review.content())
            .score(review.score())
            .createdAt(review.createdAt())
            .updatedAt(review.updatedAt())
            .build();
    }

    public static Review toDomain(final ReviewJpaEntity review) {
        return Review.builder()
            .id(review.getId())
            .productId(review.getProductId())
            .content(review.getContent())
            .score(review.getScore())
            .createdAt(review.getCreatedAt())
            .updatedAt(review.getUpdatedAt())
            .build();
    }
}
