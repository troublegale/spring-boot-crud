package ru.itmo.tg.springbootcrud.labwork.exception;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }
}
