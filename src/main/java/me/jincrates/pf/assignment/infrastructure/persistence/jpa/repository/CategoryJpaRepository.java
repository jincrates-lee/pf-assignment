package me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository;

import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

}
