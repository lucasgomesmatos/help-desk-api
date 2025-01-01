package br.com.helpdesk.commons.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class ValidationException extends StandardError{

    private List<FiledError> errors;

    @AllArgsConstructor
    @Getter
    private static class FiledError {
        private String fieldName;
        private String message;
    }

    public void addError(String fieldName, String message) {
        this.errors.add(new FiledError(fieldName, message));
    }
}
