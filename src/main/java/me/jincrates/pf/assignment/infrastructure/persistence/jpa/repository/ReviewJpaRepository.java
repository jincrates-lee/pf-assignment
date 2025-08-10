package me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository;

import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {

    void deleteAllByProductId(Long productId);
}
