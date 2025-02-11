package ru.itmo.tg.springbootcrud.labwork.exception;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String message) {
        super(message);
    }

}
