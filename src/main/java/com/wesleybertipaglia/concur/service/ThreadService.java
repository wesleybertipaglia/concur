package com.wesleybertipaglia.concur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wesleybertipaglia.concur.model.User;
import com.wesleybertipaglia.concur.model.Thread;
import com.wesleybertipaglia.concur.record.thread.ThreadRequestRecord;
import com.wesleybertipaglia.concur.record.thread.ThreadResponseRecord;
import com.wesleybertipaglia.concur.record.user.UserDetailsRecord;
import com.wesleybertipaglia.concur.repository.ThreadRepository;
import com.wesleybertipaglia.concur.repository.UserRepository;

import java.util.Optional;

@Service
public class ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<ThreadResponseRecord> getThreadById(String threadId) {
        return threadRepository.findById(threadId)
                .map(this::mapToResponseRecord);
    }

    @Transactional(readOnly = true)
    public Page<ThreadResponseRecord> getThreads(int page, int size, String sort, String direction, String query) {
        Sort sortBy = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortBy);

        if (!query.isEmpty()) {
            return threadRepository.findByTitleContainingIgnoreCase(query, pageable)
                    .map(this::mapToResponseRecord);
        }

        return threadRepository.findAll(pageable).map(this::mapToResponseRecord);
    }

    @Transactional
    public Optional<ThreadResponseRecord> createThread(ThreadRequestRecord threadRequest,
            Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        Thread thread = Thread.builder()
                .title(threadRequest.title())
                .content(threadRequest.content())
                .user(user)
                .build();

        Thread savedThread = threadRepository.save(thread);
        return Optional.of(mapToResponseRecord(savedThread));
    }

    @Transactional
    public Optional<ThreadResponseRecord> updateThread(String threadId, ThreadRequestRecord threadRequest,
            Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        if (!thread.getUser().getId().equals(user.getId())) {
            return Optional.empty();
        }

        thread.setTitle(threadRequest.title());
        thread.setContent(threadRequest.content());

        Thread updatedThread = threadRepository.save(thread);
        return Optional.of(mapToResponseRecord(updatedThread));
    }

    @Transactional
    public boolean deleteThread(String threadId, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        if (!thread.getUser().getId().equals(user.getId())) {
            return false;
        }

        threadRepository.deleteById(threadId);
        return true;
    }

    private User getUserFromAuthentication(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ThreadResponseRecord mapToResponseRecord(Thread thread) {
        UserDetailsRecord userDetailsRecord = new UserDetailsRecord(
                thread.getUser().getId(),
                thread.getUser().getName(),
                thread.getUser().getUsername(),
                thread.getUser().getEmail(),
                thread.getUser().getCreatedAt(),
                thread.getUser().getUpdatedAt());

        return new ThreadResponseRecord(
                thread.getId(),
                thread.getTitle(),
                thread.getContent(),
                userDetailsRecord,
                thread.getCreatedAt(),
                thread.getUpdatedAt());
    }
}
