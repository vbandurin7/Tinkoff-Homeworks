package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.bot.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.exception.EmptyRequestBodyException;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, EmptyRequestBodyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        return createError("Failed to update link, please check if request is correct", HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<ApiErrorResponse> createError(String message, HttpStatus httpStatus, Exception e) {
        return new ResponseEntity<>(new ApiErrorResponse(
                message, Integer.toString(httpStatus.value()),
                e.getClass().getSimpleName(), e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()), httpStatus);
    }
}
