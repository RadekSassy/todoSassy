package cz.sassy.todo.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound() {
        // Přesměrování na hlavní stránku
        return "forward:/";
    }
}
