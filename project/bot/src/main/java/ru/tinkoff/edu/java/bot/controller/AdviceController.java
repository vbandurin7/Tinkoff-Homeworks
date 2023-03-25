package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.exception.EmptyRequestBodyException;

import java.util.Arrays;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler({IllegalArgumentException.class, EmptyRequestBodyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(new ApiErrorResponse(
                "Failed to update link, please check if request is correct", "400",
                e.getClass().getSimpleName(), e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        ), HttpStatus.BAD_REQUEST);
    }
}
