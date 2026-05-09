package com.sovai.platform.common.exception;

public class SovAIAdapterException extends RuntimeException {
    public SovAIAdapterException(String message) {
        super(message);
    }

    public SovAIAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}

