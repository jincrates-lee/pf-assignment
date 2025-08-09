package me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper;

import java.util.List;
import me.jincrates.pf.assignment.domain.catalog.Category;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.CategoryJpaEntity;

public class CategoryJpaMapper {

    private CategoryJpaMapper() {
    }

    public static CategoryJpaEntity toEntity(final Category category) {
        return CategoryJpaEntity.builder()
            .id(category.id())
            .name(category.name())
            .depth(category.depth())
            .parent(category.parent() == null ? null : toEntity(category.parent()))
            .build();
    }

    public static List<CategoryJpaEntity> toEntity(final List<Category> categories) {
        return categories.stream()
            .map(CategoryJpaMapper::toEntity)
            .toList();
    }

    public static Category toDomain(final CategoryJpaEntity category) {
        return Category.builder()
            .id(category.getId())
            .name(category.getName())
            .depth(category.getDepth())
            .parent(category.getParent() == null ? null : toDomain(category.getParent()))
            .build();
    }

    public static List<Category> toDomain(final List<CategoryJpaEntity> categories) {
        return categories.stream()
            .map(CategoryJpaMapper::toDomain)
            .toList();
    }
}
