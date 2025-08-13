package me.jincrates.pf.assignment.bootstrap.http.docs;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.jincrates.pf.assignment.application.dto.CreateCategoryRequest;
import me.jincrates.pf.assignment.application.dto.CreateCategoryResponse;
import me.jincrates.pf.assignment.bootstrap.http.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Hidden
@Tag(name = "카테고리 관리", description = "카테고리 등록 API")
public interface CategoryControllerDocs {

    @Operation(summary = "카테고리 등록")
    ResponseEntity<BaseResponse<CreateCategoryResponse>> createCategory(
        @Parameter(
            description = "카테고리 등록 요청 데이터",
            required = true,
            schema = @Schema(implementation = CreateCategoryRequest.class),
            example = """
                {
                    "name": "강아지",
                    "parentId": null
                }
                """
        )
        @Valid @RequestBody CreateCategoryRequest request
    );
}
