package ru.itmo.tg.springbootcrud.labwork.exception;

public class UniqueAttributeException extends RuntimeException {

    public UniqueAttributeException(String message) {
        super(message);
    }

}
