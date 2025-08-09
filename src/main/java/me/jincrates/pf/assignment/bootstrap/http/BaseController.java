package me.jincrates.pf.assignment.bootstrap.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<BaseResponse<T>> ok(T data) {
        return buildResponse(
            HttpStatus.OK,
            null,
            data
        );
    }

    protected <T> ResponseEntity<BaseResponse<T>> created(T data) {
        return buildResponse(
            HttpStatus.CREATED,
            null,
            data
        );
    }

    protected <T> ResponseEntity<BaseResponse<T>> noContent() {
        return buildResponse(
            HttpStatus.NO_CONTENT,
            null,
            null
        );
    }

    protected <T> ResponseEntity<BaseResponse<T>> badRequest(String message) {
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            message,
            null
        );
    }

    protected <T> ResponseEntity<BaseResponse<T>> error(String message) {
        return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            message,
            null
        );
    }

    protected <T> ResponseEntity<BaseResponse<T>> response(
        HttpStatus status,
        String message,
        T data
    ) {
        return buildResponse(
            status,
            message,
            data
        );
    }

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(
        HttpStatusCode httpStatus,
        String message,
        T data
    ) {
        return ResponseEntity.status(httpStatus)
            .body(BaseResponse.of(
                httpStatus.is2xxSuccessful(),
                message,
                data
            ));
    }
}
