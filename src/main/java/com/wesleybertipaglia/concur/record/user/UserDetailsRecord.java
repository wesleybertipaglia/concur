package com.wesleybertipaglia.concur.record.user;

import java.time.LocalDateTime;

public record UserDetailsRecord(
        String id,
        String name,
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}