package me.jincrates.pf.assignment.bootstrap.http;

/**
 * 공통 응답 객체
 *
 * @param success 요청 성공 여부
 * @param message 응답 메시지
 * @param data    응답 데이터
 * @param <T>
 */
public record BaseResponse<T>(
    boolean success,
    String message,
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
