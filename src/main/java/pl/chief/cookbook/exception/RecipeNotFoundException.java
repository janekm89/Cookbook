package pl.chief.cookbook.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException() {super("Recipe not found");
    }

}
