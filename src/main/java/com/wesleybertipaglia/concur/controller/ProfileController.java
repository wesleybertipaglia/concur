package com.wesleybertipaglia.concur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wesleybertipaglia.concur.record.UserDetailsRecord;
import com.wesleybertipaglia.concur.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<UserDetailsRecord> getProfile(Authentication authentication) {
        UserDetailsRecord userDetails = profileService.getAuthenticatedUserDetails(authentication);

        return ResponseEntity.ok(userDetails);
    }
}