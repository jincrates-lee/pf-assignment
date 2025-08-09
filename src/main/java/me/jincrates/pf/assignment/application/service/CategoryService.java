package me.jincrates.pf.assignment.application.service;

import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.CategoryUseCase;
import me.jincrates.pf.assignment.application.dto.CreateCategoryRequest;
import me.jincrates.pf.assignment.application.dto.CreateCategoryResponse;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.domain.catalog.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class CategoryService implements CategoryUseCase {

    private final CategoryRepository repository;

    private static final int ROOT_CATEGORY_DEPTH = 1;

    @Override
    @Transactional
    public CreateCategoryResponse create(final CreateCategoryRequest request) {
        final Category category = isRoot(request.parentId())
            ? createRootCategory(request.name())
            : createChildCategory(
                request.name(),
                request.parentId()
            );
        final Category saved = repository.save(category);
        return toResponse(saved);
    }

    private boolean isRoot(final Long parentId) {
        return parentId == null;
    }

    private Category createRootCategory(final String name) {
        return Category.builder()
            .name(name)
            .depth(ROOT_CATEGORY_DEPTH)
            .build();
    }

    private Category createChildCategory(
        final String name,
        final Long parentId
    ) {
        Category parent = repository.findById(parentId)
            .orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));

        return Category.builder()
            .name(name)
            .depth(parent.depth() + 1)
            .parent(parent)
            .build();
    }

    private CreateCategoryResponse toResponse(final Category category) {
        return CreateCategoryResponse.builder()
            .categoryId(category.id())
            .build();
    }
}
