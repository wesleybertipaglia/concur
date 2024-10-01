package com.wesleybertipaglia.concur.record.thread;

import java.time.LocalDateTime;

import com.wesleybertipaglia.concur.record.user.UserDetailsRecord;

public record ThreadResponseRecord(
        String id,
        String title,
        String content,
        UserDetailsRecord user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
