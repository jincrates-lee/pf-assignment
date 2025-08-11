package me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository;

import java.util.List;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ProductJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    @Query("""
        SELECT DISTINCT p
        FROM ProductJpaEntity p
        JOIN p.productCategories pc
        WHERE pc.category.id = :categoryId
        ORDER BY p.sellingPrice ASC
        """)
    List<ProductJpaEntity> findAllByCategoryIdOrderBySellingPriceAsc(
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    @Query("""
        SELECT p
        FROM ProductJpaEntity p
        JOIN p.productCategories pc
        LEFT JOIN ReviewJpaEntity r on r.productId = p.id
        WHERE pc.category.id = :categoryId
        GROUP BY p
        ORDER BY COUNT(r) DESC
        """)
    List<ProductJpaEntity> findAllByCategoryIdOrderByReviewCountDesc(
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );
}
