package com.javanauta.usuario_recap.infrastructure.exceptions;

public class llegalArgumentException extends RuntimeException {
    public llegalArgumentException(String message) {
        super(message);
    }
    public llegalArgumentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
