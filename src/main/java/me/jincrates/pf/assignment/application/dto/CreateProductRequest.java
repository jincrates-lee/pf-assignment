package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Set;

@Schema(description = "상품 생성 요청")
public record CreateProductRequest(
    @Schema(description = "상품 이름", example = "촉촉트릿 북어 80g", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "상품 이름은 필수입니다.")
    String name,
    @Schema(description = "상품 판매가", example = "15000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "상품 판매가는 필수입니다.")
    @Positive(message = "상품 판매가는 0원보다 커야합니다.")
    Long sellingPrice,
    @Schema(description = "상품 할인가", example = "2100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "상품 할인가는 필수입니다.")
    @PositiveOrZero(message = "상품 할인가는 0원 이상이여야 합니다.")
    Long discountPrice,
    @Schema(description = "상품 브랜드", example = "촉촉트릿", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "상품 브랜드는 필수입니다.")
    String brand,
    @Schema(description = "상품 카테고리 ID 목록", example = "[1, 2, 3]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "상품 카테고리 목록은 필수입니다.")
    Set<Long> categoryIds
) {

}
