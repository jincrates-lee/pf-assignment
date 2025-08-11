package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "상품 생성 응답")
@Builder
public record CreateProductResponse(
    @Schema(description = "생성된 상품 ID", example = "1")
    Long productId
) {

}
