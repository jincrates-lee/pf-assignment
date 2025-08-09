package me.jincrates.pf.assignment.bootstrap.http.advice;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.assignment.bootstrap.http.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버에서 에러가 발생했습니다.";
    private static final String VALIDATION_ERROR_FORMAT = "%s 입력된 값: [%s=%s]";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception exception,
        Object body,
        HttpHeaders headers,
        HttpStatusCode statusCode,
        WebRequest request
    ) {
        log.warn(
            "ExceptionInternal occurred: {}",
            exception.getMessage(),
            exception
        );
        return error(
            statusCode,
            exception.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        log.warn(
            "MethodArgumentNotValid occurred: {}",
            exception.getMessage()
        );
        String message = formatBindingResultMessage(exception.getBindingResult());
        return error(
            HttpStatus.BAD_REQUEST,
            message
        );
    }

    /**
     * IllegalArgumentException 에러
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.warn(
            "IllegalArgumentException occurred: {}",
            exception.getMessage()
        );
        return error(
            HttpStatus.BAD_REQUEST,
            exception.getMessage()
        );
    }

    /**
     * 예상하지 못한 에러
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception exception) {
        log.error(
            "Exception occurred: {}",
            exception.getMessage(),
            exception
        );
        return error(
            HttpStatus.INTERNAL_SERVER_ERROR,
            INTERNAL_SERVER_ERROR_MESSAGE
        );
    }

//    /**
//     * 비즈니스 에러(커스텀)
//     */
//    @ExceptionHandler(BusinessException.class)
//    protected ResponseEntity<?> handleBusinessException(BusinessException exception) {
//        log.warn(
//            "BusinessException occurred - message: {}, arguments: {}",
//            exception.getMessage(),
//            exception.getArguments()
//        );
//        return error(
//            HttpStatus.BAD_REQUEST,
//            exception.getMessage()
//        );
//    }
//
//    /**
//     * 서버 에러(커스텀)
//     */
//    @ExceptionHandler(ServerException.class)
//    protected ResponseEntity<?> handleServerException(ServerException exception) {
//        log.warn(
//            "ServerException occurred - message: {}, arguments: {}",
//            exception.getMessage(),
//            exception.getArguments()
//        );
//        return error(
//            HttpStatus.BAD_REQUEST,
//            exception.getMessage()
//        );
//    }


    private ResponseEntity<Object> error(
        HttpStatusCode httpStatus,
        String message
    ) {
        return ResponseEntity.status(httpStatus)
            .body(BaseResponse.of(
                httpStatus.is2xxSuccessful(),
                message,
                null
            ));
    }

    private String formatBindingResultMessage(BindingResult bindingResult) {
        if (bindingResult.getFieldErrors().isEmpty()) {
            return "입력값이 유효하지 않습니다.";
        }

        return bindingResult.getFieldErrors().stream()
            .map(error -> String.format(
                VALIDATION_ERROR_FORMAT,
                error.getDefaultMessage(),
                error.getField(),
                error.getRejectedValue()
            ))
            .collect(Collectors.joining("\n"));
    }
}
