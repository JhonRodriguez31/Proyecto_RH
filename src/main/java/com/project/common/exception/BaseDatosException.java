package com.project.common.exception;

public class BaseDatosException extends RuntimeException {
    public BaseDatosException(String message, Throwable causa) {
        super(message, causa);
    }
}
