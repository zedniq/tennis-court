package com.IQproject.court.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for validation and illegal argument errors.
 * This controller advice captures validation errors and returns meaningful
 * error responses to the client.
 *
 * @author Vojtech Zednik
 */
@ControllerAdvice
public class ValidationHandler {

    /**
     * Handles validation errors from @Valid annotations.
     * Converts field errors into a key-value map of field names and messages.
     *
     * @param ex the exception containing validation errors
     * @return 400 Bad Request with a map of field error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (m1, m2) -> m1));
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles IllegalArgumentExceptions thrown in the application.
     *
     * @param ex the exception
     * @return 400 Bad Request with a single error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}