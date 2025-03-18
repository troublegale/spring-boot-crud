package ru.itmo.tg.springbootcrud.labwork.exception;

public class ImportHistoryNotFoundException extends RuntimeException {
    public ImportHistoryNotFoundException(String message) {
        super(message);
    }
}
