package com.example.orderservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return new ResponseEntity(illegalArgumentException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity handleIllegalArgumentException(MethodArgumentNotValidException ex) {
        return new ResponseEntity(ex.getBindingResult().getFieldErrors().stream().map(n -> new String(n.getDefaultMessage())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new ResponseEntity(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity(Collections.singletonMap("message", "Malformed JSON request " + ex.getMessage()),
                HttpStatus.BAD_REQUEST);

    }
}

