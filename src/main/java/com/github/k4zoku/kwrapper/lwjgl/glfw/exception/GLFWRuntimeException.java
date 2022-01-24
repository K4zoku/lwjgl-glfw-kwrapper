package com.github.k4zoku.kwrapper.lwjgl.glfw.exception;

public class GLFWRuntimeException extends RuntimeException {
    public GLFWRuntimeException() {
        super();
    }

    public GLFWRuntimeException(String message) {
        super(message);
    }

    public GLFWRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GLFWRuntimeException(Throwable cause) {
        super(cause);
    }

    public GLFWRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
