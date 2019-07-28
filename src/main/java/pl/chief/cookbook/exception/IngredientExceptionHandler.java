package pl.chief.cookbook.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class IngredientExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IngredientNotFoundException.class)
    public ResponseEntity handleIngredientNotFoundException(){
        return ResponseEntity.notFound().build();

    }

}