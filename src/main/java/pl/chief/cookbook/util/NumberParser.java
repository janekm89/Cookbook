package pl.chief.cookbook.util;

import pl.chief.cookbook.exception.NotNumberException;

import static pl.chief.cookbook.validation.CommonTraitsValidator.validIfIsNumber;

public class NumberParser {

    public static int parseIfIsNumber(String number) {
        if (!validIfIsNumber(number)) {
            throw new NotNumberException(number);
        } else {
            return Integer.parseInt(number);
        }
    }
}
