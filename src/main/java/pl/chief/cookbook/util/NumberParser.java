package pl.chief.cookbook.util;

import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.validation.RecipeControllerValidator;

@Service
public class NumberParser {

    public static int parseIfIsNumber(String number) {
        if (!RecipeControllerValidator.validIfIsNumber(number)) {
            throw new NotNumberException(number);
        } else {
            return Integer.parseInt(number);
        }
    }
}
