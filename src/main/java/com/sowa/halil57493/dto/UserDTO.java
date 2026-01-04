package com.sowa.halil57493.dto;

import com.sowa.halil57493.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @Email(message = "Invalid email format")
    @NotNull(message = "Email cannot be null")
    private String email;

    @StrongPassword
    private String password;
}
