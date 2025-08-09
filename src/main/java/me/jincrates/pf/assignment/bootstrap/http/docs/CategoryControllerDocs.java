package me.jincrates.pf.assignment.bootstrap.http.docs;

import me.jincrates.pf.assignment.application.dto.CreateCategoryRequest;
import me.jincrates.pf.assignment.application.dto.CreateCategoryResponse;
import me.jincrates.pf.assignment.bootstrap.http.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryControllerDocs {

    ResponseEntity<BaseResponse<CreateCategoryResponse>> createCategory(CreateCategoryRequest request);
}
