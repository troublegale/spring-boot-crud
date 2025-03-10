package ru.itmo.tg.springbootcrud.labwork.exception;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.PersistentObjectException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientPermissionsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleInsufficientPermissionsException(InsufficientPermissionsException e) {
        return new ResponseEntity<>("Insufficient permissions: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LabWorkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleLabWorkNotFoundException(LabWorkNotFoundException e) {
        return new ResponseEntity<>("LabWork with such ID does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePersonNotFoundException(PersonNotFoundException e) {
        return new ResponseEntity<>("Person with such ID does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisciplineNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleDisciplineNotFoundException(DisciplineNotFoundException e) {
        return new ResponseEntity<>("Discipline with such ID does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>("Element not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>("Illegal argument: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePropertyReferenceException(PropertyReferenceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Constraints on some fields are violated", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Invalid request body content", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AbsentNestedObjectsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAbsentNestedObjectsException(AbsentNestedObjectsException e) {
        return new ResponseEntity<>("Necessary information about nested objects was not provided",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UniqueAttributeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleUniqueAttributeException(UniqueAttributeException e) {
        return new ResponseEntity<>("Unique attributes duplicated: " + e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleUsernameTakenException(UsernameTakenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoleChangeTicketNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleRoleChangeTicketNotFoundException(RoleChangeTicketNotFoundException e) {
        return new ResponseEntity<>("Role Change Ticket with such ID does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketAlreadyResolvedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleTicketAlreadyResolved(TicketAlreadyResolvedException e) {
        return new ResponseEntity<>("Ticket with such ID has already been resolved", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordTooShortException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePasswordTooShortException(PasswordTooShortException e) {
        return new ResponseEntity<>("Password is too short", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersistentObjectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePersistentObjectException(PersistentObjectException e) {
        return new ResponseEntity<>("ID is not to be stated when creating an object", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadFileException(BadFileException e) {
        return new ResponseEntity<>("Bad file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
