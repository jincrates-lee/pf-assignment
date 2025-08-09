package me.jincrates.pf.assignment.domain.exception;

import java.util.Map;

public class ServerException extends BaseException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(
        String message,
        Map<String, Object> arguments
    ) {
        super(
            message,
            arguments
        );
    }

    public ServerException(
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
