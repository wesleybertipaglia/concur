package com.wesleybertipaglia.concur.record.thread;

import jakarta.validation.constraints.NotBlank;

public record ThreadRequestRecord(
        @NotBlank(message = "Thread request title cannot be blank") String title,
        @NotBlank(message = "Thread request content cannot be blank") String content) {
}
