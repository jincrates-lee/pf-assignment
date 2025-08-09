package me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper;

import java.util.List;
import me.jincrates.pf.assignment.domain.model.Category;
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
        if (category == null) {
            return null;
        }
        Category parent = null;
        if (category.getParent() != null) {
            parent = Category.builder()
                .id(category.getParent().getId())
                .name(category.getParent().getName())
                .depth(category.getParent().getDepth())
                .build();
        }
        return Category.builder()
            .id(category.getId())
            .name(category.getName())
            .depth(category.getDepth())
            .parent(parent)
            .build();
    }

    public static List<Category> toDomain(final List<CategoryJpaEntity> categories) {
        return categories.stream()
            .map(CategoryJpaMapper::toDomain)
            .toList();
    }
}
