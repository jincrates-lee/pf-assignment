package me.jincrates.pf.assignment.domain.exception;


import java.util.Collections;
import java.util.Map;

abstract class BaseException extends RuntimeException {

    private final Map<String, Object> arguments;

    protected BaseException(String message) {
        super(message);
        this.arguments = Collections.emptyMap();
    }

    protected BaseException(
        String message,
        Map<String, Object> arguments
    ) {
        super(message);
        this.arguments = (arguments != null) ? Map.copyOf(arguments) : Collections.emptyMap();
    }

    protected BaseException(
        String message,
        Map<String, Object> arguments,
        Throwable cause
    ) {
        super(
            message,
            cause
        );
        this.arguments = (arguments != null) ? Map.copyOf(arguments) : Collections.emptyMap();
    }

    protected Map<String, Object> getArguments() {
        return arguments;
    }
}
