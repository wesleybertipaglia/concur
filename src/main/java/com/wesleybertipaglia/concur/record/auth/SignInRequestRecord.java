package com.wesleybertipaglia.concur.record.auth;

import jakarta.validation.constraints.NotBlank;

public record SignInRequestRecord(
                @NotBlank(message = "Sign In Request username cannot be blank") String username,
                @NotBlank(message = "Sign In Request password cannot be blank") String password) {
}
