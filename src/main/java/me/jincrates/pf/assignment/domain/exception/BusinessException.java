package me.jincrates.pf.assignment.domain.exception;

import java.util.Map;

public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(
        String message,
        Map<String, Object> arguments
    ) {
        super(
            message,
            arguments
        );
    }

    public BusinessException(
        String message,
        Map<String, Object> arguments,
        Throwable cause
    ) {
        super(
            message,
            arguments,
            cause
        );
    }

    @Override
    public Map<String, Object> getArguments() {
        return super.getArguments();
    }
}
