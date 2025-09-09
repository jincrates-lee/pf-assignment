package me.jincrates.pf.assignment.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.repository.NativeJpaRepository;
import me.jincrates.pf.assignment.domain.model.Sample;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.SampleJpaEntity;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper.SampleJpaMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NativeJpaRepositoryAdapter implements NativeJpaRepository {

    private final EntityManager entityManager;

    @Override
    public List<Sample> saveAll(final List<Sample> samples) {
        List<SampleJpaEntity> entities = samples.stream()
            .map(SampleJpaMapper::toEntity)
            .toList();

        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            for (SampleJpaEntity entity : entities) {
                entityManager.persist(entity);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
        }

        return entities.stream()
            .map(SampleJpaMapper::toDomain)
            .toList();
    }
}
