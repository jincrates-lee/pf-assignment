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
        if (isRoot(request.parentId())) {
            Category saved = createCategory(request.name());

            return toResponse(saved);
        }

        Category parent = repository.findById(request.parentId())
            .orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));

        Category saved = createChildCategory(
            request.name(),
            parent
        );

        return toResponse(saved);
    }

    private boolean isRoot(final Long parentId) {
        return parentId == null;
    }

    private Category createCategory(final String name) {
        Category category = Category.builder()
            .name(name)
            .depth(ROOT_CATEGORY_DEPTH)
            .build();
        return repository.save(category);
    }

    private Category createChildCategory(
        final String name,
        final Category parent
    ) {
        Category category = Category.builder()
            .name(name)
            .depth(parent.depth() + 1)
            .parent(parent)
            .build();
        return repository.save(category);
    }

    private CreateCategoryResponse toResponse(final Category category) {
        return CreateCategoryResponse.builder()
            .categoryId(category.id())
            .build();
    }
}
