package me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper;

import me.jincrates.pf.assignment.domain.model.Sample;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.SampleJpaEntity;

public class SampleJpaMapper {

    public static SampleJpaEntity toEntity(Sample sample) {
        return SampleJpaEntity.builder()
            .id(sample.id())
            .message(sample.message())
            .build();
    }

    public static Sample toDomain(SampleJpaEntity sample) {
        return Sample.builder()
            .id(sample.getId())
            .message(sample.getMessage())
            .build();
    }
}
