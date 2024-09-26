package com.wesleybertipaglia.concur.record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestRecord(
        @NotBlank(message = "Sign Up Request username cannot be blank") String username,
        @NotBlank(message = "Sign Up Request name cannot be blank") String name,
        @Email(message = "Sign Up Request email should be valid") String email,
        @NotBlank(message = "Sign Up Request password cannot be blank") String password) {
}