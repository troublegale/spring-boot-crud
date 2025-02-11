package ru.itmo.tg.springbootcrud.labwork.exception;

public class InsufficientPermissionsException extends RuntimeException {

    public InsufficientPermissionsException(String message) {
        super(message);
    }

}
