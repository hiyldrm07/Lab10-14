package com.sowa.halil57493.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        // Basic check: length > 8 and contains at least one non-alphanumeric char
        return password.length() >= 8 && !password.matches("[a-zA-Z0-9 ]*");
    }
}
