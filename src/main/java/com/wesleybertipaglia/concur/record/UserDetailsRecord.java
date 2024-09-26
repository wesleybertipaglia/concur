package com.wesleybertipaglia.concur.record;

import java.util.UUID;

public record UserDetailsRecord(UUID id, String name, String username, String email) {
}