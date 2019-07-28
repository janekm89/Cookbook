package pl.chief.cookbook.validation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CommonTraitsValidator {

    public static boolean validIfIsNumber(String number) {
        for (char c : number.toCharArray()) {
            if (String.valueOf(c).matches("\\D"))
                return false;
        }
        return true;
    }

    public static boolean validName(@NotNull @Valid String name) {
        return name.length() > 0;
    }

}
