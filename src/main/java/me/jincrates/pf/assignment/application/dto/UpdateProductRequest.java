package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Set;
import lombok.With;

@Schema(description = "상품 수정 요청")
public record UpdateProductRequest(
    @Schema(description = "상품 ID", hidden = true)
    @With
    Long id,
    @Schema(description = "상품 이름", example = "촉촉트릿 북어 80g", requiredMode = RequiredMode.NOT_REQUIRED)
    String name,
    @Schema(description = "상품 판매가", example = "15000", requiredMode = RequiredMode.NOT_REQUIRED)
    @Positive(message = "상품 판매가는 0원보다 커야합니다.")
    Long sellingPrice,
    @Schema(description = "상품 할인가", example = "2100", requiredMode = RequiredMode.NOT_REQUIRED)
    @PositiveOrZero(message = "상품 할인가는 0원 이상이여야 합니다.")
    Long discountPrice,
    @Schema(description = "브랜드명", example = "촉촉트릿", requiredMode = RequiredMode.NOT_REQUIRED)
    String brand,
    @Schema(description = "카테고리 ID 목록", example = "[1, 2, 3]", requiredMode = RequiredMode.NOT_REQUIRED)
    Set<Long> categoryIds
) {

}
