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

    protected <T> ResponseEntity<BaseResponse<T>> ok() {
        return ok(null);
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
