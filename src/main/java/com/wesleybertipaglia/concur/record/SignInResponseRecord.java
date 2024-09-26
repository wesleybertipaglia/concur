package com.wesleybertipaglia.concur.record;

import java.time.Instant;

public record SignInResponseRecord(String token, Instant expiration) {
}