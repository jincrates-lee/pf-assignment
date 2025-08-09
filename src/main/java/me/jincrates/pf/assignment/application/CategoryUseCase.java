package me.jincrates.pf.assignment.application;

import me.jincrates.pf.assignment.application.dto.CreateCategoryRequest;
import me.jincrates.pf.assignment.application.dto.CreateCategoryResponse;

public interface CategoryUseCase {

    CreateCategoryResponse create(CreateCategoryRequest request);
}
