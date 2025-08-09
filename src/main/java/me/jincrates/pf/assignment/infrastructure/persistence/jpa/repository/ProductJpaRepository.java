package me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository;

import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

}
