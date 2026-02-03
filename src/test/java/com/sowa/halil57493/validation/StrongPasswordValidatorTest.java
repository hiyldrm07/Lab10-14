package com.sowa.halil57493.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StrongPasswordValidatorTest {

    private StrongPasswordValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new StrongPasswordValidator();
    }

    @Test
    void isValid_NullPassword_ShouldReturnFalse() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_ShortPassword_ShouldReturnFalse() {
        assertFalse(validator.isValid("short1!", context));
    }

    @Test
    void isValid_PasswordWithoutSpecialCharacter_ShouldReturnFalse() {
        assertFalse(validator.isValid("password123", context));
    }

    @Test
    void isValid_PasswordWithSpaceAsSpecialCharacter_CheckBehavior() {
        // The regex !password.matches("[a-zA-Z0-9 ]*") means if it contains ONLY alphanumeric and space, return false (invalid).
        // So a space is NOT considered a special character required for validity in this specific regex implementation?
        // Let's verify standard behavior. If regex is "[a-zA-Z0-9 ]*", it matches strings with only those chars.
        // !matches means it MUST contain something else.
        // So "password 123" (with space) matches "[a-zA-Z0-9 ]*", so !matches is false.
        // So space is NOT sufficient.
        assertFalse(validator.isValid("password 123", context));
    }

    @Test
    void isValid_ValidStrongPassword_ShouldReturnTrue() {
        assertTrue(validator.isValid("Password123!", context));
    }

    @Test
    void isValid_ValidStrongPasswordWithSymbol_ShouldReturnTrue() {
        assertTrue(validator.isValid("Password@123", context));
    }
}
