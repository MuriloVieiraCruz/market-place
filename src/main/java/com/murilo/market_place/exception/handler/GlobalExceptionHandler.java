package com.murilo.market_place.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.murilo.market_place.exception.enums.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, Map<String, Object>> handle(){
        return createErrorMap(ApiError.INVALID_BODY,
                "The request body has some errors or doesn't exist");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDefinitionException.class)
    public Map<String, Map<String, Object>> handle(InvalidDefinitionException ide){
        String attribute = ide.getPath().getLast().getFieldName();
        String errorMessage = "The attribute '" + attribute + "' has an invalid format";
        return createErrorMap(ApiError.INVALID_FORMAT, errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, Map<String, Object>> handle(IllegalArgumentException ie){
        return createErrorMap(ApiError.INVALID_PARAMETER, ie.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public Map<String, Map<String, Object>> handle(NullPointerException npe){
        return createErrorMap(ApiError.INVALID_PARAMETER, npe.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException.class)
    public Map<String, Map<String, Object>> handle(MissingPathVariableException mpve){
        return createErrorMap(ApiError.REQUIRED_PRECONDITION, mpve.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, Map<String, Object>> handle(MethodArgumentTypeMismatchException matme){
        return createErrorMap(ApiError.INVALID_TYPE_PARAMETER, "The URI has invalid values");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Map<String, Map<String, Object>> handle(HttpRequestMethodNotSupportedException hrmnse){
        return createErrorMap(ApiError.HTTP_METHOD_NOT_SUPPORTED, hrmnse.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String, Map<String, Object>> handle(MissingServletRequestParameterException mrpe){
        return createErrorMap(ApiError.MANDATORY_PARAMETER, mrpe.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, Map<String, Object>> handlePSQLExceptions(
            DataIntegrityViolationException dve) {
        return createErrorMap(ApiError.INVALID_FIELD,
                "An reference integration error occurred on database");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Map<String, Object>> handle(MethodArgumentNotValidException mane){
        var errors = mane.getFieldErrors();
        Map<String, Map<String, Object>> body = new HashMap<>();
        Map<String, Object> details = new HashMap<>();
        errors.forEach((error) -> {
            details.put("code", ApiError.INVALID_FIELD.getCode());
            details.put("message", error.getDefaultMessage());
        });
        body.put("errors", details);
        return body;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Map<String, Object>> handle(ConstraintViolationException cve){

        Map<String, Map<String, Object>> body = new HashMap<>();
        Map<String, Object> description = new HashMap<>();

        cve.getConstraintViolations().forEach((error) -> {
            description.put("error", error.getMessage());
        });

        body.put("errors", description);
        return body;
    }

    private Map<String, Map<String, Object>> createErrorMap(ApiError apiError, String errorMessage) {
        Map<String, Map<String, Object>> body = new HashMap<>();

        Map<String, Object> description = new HashMap<>();
        description.put("message", errorMessage);
        description.put("code", apiError.getCode());

        body.put("errors", description);

        return body;
    }
}
