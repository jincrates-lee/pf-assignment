package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "카테고리 생성 요청")
public record CreateCategoryRequest(
    @Schema(description = "카테고리명", example = "강아지", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    String name,
    @Schema(description = "상위 카테고리 ID (없으면 루트 카테고리)", example = "1", requiredMode = RequiredMode.NOT_REQUIRED)
    Long parentId
) {

}
