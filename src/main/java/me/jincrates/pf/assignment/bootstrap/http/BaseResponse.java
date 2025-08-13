package me.jincrates.pf.assignment.bootstrap.http;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 공통 응답 객체
 *
 * @param success 요청 성공 여부
 * @param message 응답 메시지
 * @param data    응답 데이터
 * @param <T>
 */
@Schema(description = "공통 응답 객체")
public record BaseResponse<T>(
    @Schema(description = "요청 성공 여부", example = "true")
    boolean success,
    @Schema(description = "응답 메시지", nullable = true)
    String message,
    @Schema(description = "응답 데이터")
    T data
) {

    public static <T> BaseResponse<T> of(
        boolean success,
        String message,
        T data
    ) {
        return new BaseResponse<>(
            success,
            message,
            data
        );
    }
}
