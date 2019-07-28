package pl.chief.cookbook.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RecipeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = RecipeNotFoundException.class)
    public ResponseEntity handleRecipeNotFoundException() {
        return ResponseEntity.notFound().build();

    }

}