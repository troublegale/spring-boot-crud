package ru.itmo.tg.springbootcrud.labwork.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;

import java.util.HashMap;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LabWorkValidator {

    private final Validator validator;

    public void validateLabWork(LabWork labWork) throws ValidationException {
        Set<ConstraintViolation<LabWork>> violations = validator.validate(labWork);
        var errors = new HashMap<String, String>();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<LabWork> violation : violations) {
                String fieldName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                errors.put(fieldName, errorMessage);
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException();
        }
    }

}
