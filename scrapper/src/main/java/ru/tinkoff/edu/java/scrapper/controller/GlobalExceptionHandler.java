package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;

import java.util.Arrays;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> typeMismatchHandler(TypeMismatchException e) {
        return createError("Incorrect request parameters", HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler({ChatNotFoundException.class, LinkNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> chatNotFoundHandler(Exception e) {
        return createError("Chat doesn't exist", HttpStatus.NOT_FOUND, e);
    }

    private ResponseEntity<ApiErrorResponse> createError(String message, HttpStatus httpStatus, Exception e) {
        return new ResponseEntity<>(new ApiErrorResponse(
                message, Integer.toString(httpStatus.value()),
                e.getClass().getSimpleName(), e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()), httpStatus);
    }
}
