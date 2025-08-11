package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "카테고리 생성 응답")
@Builder
public record CreateCategoryResponse(
    @Schema(description = "생성된 카테고리 ID", example = "1")
    Long categoryId
) {

}
