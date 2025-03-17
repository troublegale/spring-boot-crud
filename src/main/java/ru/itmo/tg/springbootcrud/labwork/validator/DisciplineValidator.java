package ru.itmo.tg.springbootcrud.labwork.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;

import java.util.HashMap;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DisciplineValidator {

    private final Validator validator;

    public void validateDiscipline(Discipline discipline) {
        Set<ConstraintViolation<Discipline>> violations = validator.validate(discipline);
        var errors = new HashMap<String, String>();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Discipline> violation : violations) {
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
