package com.wesleybertipaglia.concur.record.auth;

import java.time.Instant;

public record SignInResponseRecord(String token, Instant expiration) {
}