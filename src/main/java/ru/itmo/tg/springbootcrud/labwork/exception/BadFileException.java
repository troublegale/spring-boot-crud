package ru.itmo.tg.springbootcrud.labwork.exception;

public class BadFileException extends RuntimeException {
    public BadFileException(String message) {
        super(message);
    }
}
