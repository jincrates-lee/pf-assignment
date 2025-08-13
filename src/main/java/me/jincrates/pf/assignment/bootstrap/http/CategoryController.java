package me.jincrates.pf.assignment.bootstrap.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.CategoryUseCase;
import me.jincrates.pf.assignment.application.dto.CreateCategoryRequest;
import me.jincrates.pf.assignment.application.dto.CreateCategoryResponse;
import me.jincrates.pf.assignment.bootstrap.http.docs.CategoryControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
class CategoryController extends BaseController implements CategoryControllerDocs {

    private final CategoryUseCase useCase;

    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<CreateCategoryResponse>> createCategory(
        @Valid @RequestBody CreateCategoryRequest request
    ) {
        return created(useCase.create(request));
    }
}
