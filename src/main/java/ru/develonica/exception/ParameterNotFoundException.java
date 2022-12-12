package ru.develonica.exception;

public class ParameterNotFoundException extends RuntimeException {

    public ParameterNotFoundException(String message) {
        super(message);
    }
}
