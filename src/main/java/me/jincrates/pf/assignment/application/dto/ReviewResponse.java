package me.jincrates.pf.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Schema(description = "리뷰 응답")
@Builder
public record ReviewResponse(
    @Schema(description = "리뷰 ID", example = "1")
    Long id,
    @Schema(description = "상품 ID", example = "1")
    Long productId,
    @Schema(description = "상품 이름", example = "촉촉트릿 북어 80g")
    String productName,
    @Schema(description = "리뷰 점수", example = "5")
    Integer score,
    @Schema(description = "리뷰 내용", example = "우리 강아지가 정말 좋아해요!")
    String content,
    @Schema(description = "등록일시", example = "2999-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

}
