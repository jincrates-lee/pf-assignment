package me.jincrates.pf.assignment.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.domain.catalog.Category;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.CategoryJpaEntity;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper.CategoryJpaMapper;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository.CategoryJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository repository;

    @Override
    public Category save(final Category category) {
        CategoryJpaEntity entity = CategoryJpaMapper.toEntity(category);
        CategoryJpaEntity saved = repository.save(entity);
        return CategoryJpaMapper.toDomain(saved);
    }

    @Override
    public void saveAll(final List<Category> categories) {
        List<CategoryJpaEntity> entities = CategoryJpaMapper.toEntity(categories);
        repository.saveAll(entities);
    }

    @Override
    public Optional<Category> findById(final Long categoryId) {
        return repository.findById(categoryId)
            .map(CategoryJpaMapper::toDomain);
    }

    @Override
    public List<Category> findAllByIdIn(final List<Long> ids) {
        return repository.findAllById(ids).stream()
            .map(CategoryJpaMapper::toDomain)
            .toList();
    }

    public void deleteAllInBatch() {
        repository.deleteAllInBatch();
    }
}
