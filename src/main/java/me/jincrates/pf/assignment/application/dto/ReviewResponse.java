package me.jincrates.pf.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReviewResponse(
    Long id,
    Long productId,
    String productName,
    Integer score,
    String content,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

}
