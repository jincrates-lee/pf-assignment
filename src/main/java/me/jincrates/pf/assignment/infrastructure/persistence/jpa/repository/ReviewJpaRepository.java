package me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository;

import java.util.List;
import me.jincrates.pf.assignment.domain.vo.ProductAverageScore;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ReviewJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {

    void deleteAllByProductId(Long productId);

    List<ReviewJpaEntity> findAllByProductId(
        Long productId,
        Pageable pageable
    );

    List<ReviewJpaEntity> findAllByProductIdIn(List<Long> productIds);

    @Query("""
            SELECT new me.jincrates.pf.assignment.domain.vo.ProductAverageScore(
                r.productId,
                AVG(CAST(r.score AS double))
            )
            FROM ReviewJpaEntity r
            WHERE r.productId IN :productIds
            GROUP BY r.productId
        """)
    List<ProductAverageScore> findAverageScoreByProductIdIn(@Param("productIds") List<Long> productIds);
}
