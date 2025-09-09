package me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository;

import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.SampleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleJpaRepository extends JpaRepository<SampleJpaEntity, Long> {

}
