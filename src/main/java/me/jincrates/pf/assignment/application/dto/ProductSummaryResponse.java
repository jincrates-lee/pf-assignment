package me.jincrates.pf.assignment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "상품 요약 응답")
@Builder
public record ProductSummaryResponse(
    @Schema(description = "상품 ID", example = "1")
    Long id,
    @Schema(description = "상품 이름", example = "촉촉트릿 북어 80g")
    String name,
    @Schema(description = "상품 판매가", example = "15000")
    Long sellingPrice,
    @Schema(description = "상품 할인가", example = "2100")
    Long discountPrice,
    @Schema(description = "상품 브랜드", example = "촉촉트릿")
    String brand,
    @Schema(description = "할인율(%)", example = "86")
    Long discountRate,
    @Schema(description = "리뷰 평점", example = "4.5")
    Double reviewAverageScore,
    @Schema(description = "카테고리 정보 목록")
    List<CategoryResponse> categories
) {

    @Schema(description = "카테고리 정보")
    @Builder
    public record CategoryResponse(
        @Schema(description = "카테고리 ID", example = "1")
        Long id,
        @Schema(description = "카테고리 이름", example = "강아지")
        String name,
        @Schema(description = "카테고리 단계", example = "1")
        Integer depth,
        @Schema(description = "상위 카테고리")
        CategoryResponse parent
    ) {

    }
}
