package me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "product_categories")
@Comment("상품 카테고리")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategoryJpaEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaEntity category;

    protected ProductCategoryJpaEntity(
        final ProductJpaEntity product,
        final CategoryJpaEntity category
    ) {
        this.product = product;
        this.category = category;
    }
}
