package com.wesleybertipaglia.concur.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wesleybertipaglia.concur.model.User;
import com.wesleybertipaglia.concur.record.UserDetailsRecord;
import com.wesleybertipaglia.concur.repository.UserRepository;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDetailsRecord getAuthenticatedUserDetails(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDetailsRecord(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail());
    }
}