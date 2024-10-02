package com.wesleybertipaglia.concur.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.wesleybertipaglia.concur.model.User;
import com.wesleybertipaglia.concur.record.auth.SignInRequestRecord;
import com.wesleybertipaglia.concur.record.auth.SignInResponseRecord;
import com.wesleybertipaglia.concur.record.auth.SignUpRequestRecord;
import com.wesleybertipaglia.concur.record.email.EmailRecord;
import com.wesleybertipaglia.concur.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Transactional
    public void signUp(SignUpRequestRecord signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())) {
            throw new RuntimeException("Username already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .name(signUpRequest.name())
                .username(signUpRequest.username())
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();

        User savedUser = userRepository.save(user);

        if (savedUser.getId() != null) {
            sendSignUpEmail(savedUser);
        }
    }

    @Transactional(readOnly = true)
    public SignInResponseRecord signIn(SignInRequestRecord signInRequest) {
        User user = userRepository.findByUsername(signInRequest.username())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = createToken(user);
        Instant expiration = Instant.now().plus(Duration.ofHours(1));

        return new SignInResponseRecord(token, expiration);
    }

    private String createToken(User user) {
        try {
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuer("concur")
                    .subject(user.getId().toString())
                    .claim("roles", "ROLE_USER")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(Duration.ofHours(1)))
                    .build();

            return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }

    private void sendSignUpEmail(User user) {
        String emailSubject = "Welcome to Concur";

        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("verificationLink", "your_verification_link_here");

        EmailRecord email = new EmailRecord(user.getEmail(), emailSubject, "");
        String htmlContent = templateEngine.process("email/signup-email", context);
        emailService.sendMail(email, htmlContent);
    }
}