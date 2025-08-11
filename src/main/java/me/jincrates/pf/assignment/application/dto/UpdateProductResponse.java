package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "상품 수정 응답")
@Builder
public record UpdateProductResponse(
    @Schema(description = "수정된 상품 ID", example = "1")
    Long productId
) {

}
