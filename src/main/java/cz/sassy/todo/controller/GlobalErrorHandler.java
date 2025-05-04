package cz.sassy.todo.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * A class for global error handling in the application.
 * Contains a method for handling NoHandlerFoundException exceptions.
 */

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound() {
        return "forward:/";
    }
}
