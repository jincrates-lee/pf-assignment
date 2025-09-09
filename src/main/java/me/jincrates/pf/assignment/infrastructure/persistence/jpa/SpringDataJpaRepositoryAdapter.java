package me.jincrates.pf.assignment.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.repository.SpringDataJapRepository;
import me.jincrates.pf.assignment.domain.model.Sample;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.SampleJpaEntity;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper.SampleJpaMapper;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository.SampleJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SpringDataJpaRepositoryAdapter implements SpringDataJapRepository {

    private final SampleJpaRepository repository;

    @Override
    @Transactional
    public List<Sample> saveAll(final List<Sample> samples) {
        List<SampleJpaEntity> entity = samples.stream()
            .map(SampleJpaMapper::toEntity)
            .toList();

        List<SampleJpaEntity> saved = repository.saveAll(entity);

        return saved.stream()
            .map(SampleJpaMapper::toDomain)
            .toList();
    }
}
