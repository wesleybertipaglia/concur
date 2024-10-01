package com.wesleybertipaglia.concur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.wesleybertipaglia.concur.record.thread.ThreadRequestRecord;
import com.wesleybertipaglia.concur.record.thread.ThreadResponseRecord;
import com.wesleybertipaglia.concur.service.ThreadService;

import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/threads")
public class ThreadController {

    @Autowired
    private ThreadService threadService;

    @GetMapping("/{id}")
    public ResponseEntity<ThreadResponseRecord> getThreadById(@PathVariable String id) {
        Optional<ThreadResponseRecord> thread = threadService.getThreadById(id);
        return thread.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<Page<ThreadResponseRecord>> getThreads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "") String query) {
        Page<ThreadResponseRecord> threads = threadService.getThreads(page, size, sort, direction, query);
        return ResponseEntity.ok(threads);
    }

    @PostMapping
    public ResponseEntity<ThreadResponseRecord> createThread(
            @Valid @RequestBody ThreadRequestRecord threadRequest,
            Authentication authentication) {
        Optional<ThreadResponseRecord> createdThread = threadService.createThread(threadRequest, authentication);
        return createdThread.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThreadResponseRecord> updateThread(
            @PathVariable String id,
            @Valid @RequestBody ThreadRequestRecord threadRequest,
            Authentication authentication) {
        Optional<ThreadResponseRecord> updatedThread = threadService.updateThread(id, threadRequest, authentication);
        return updatedThread.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThread(
            @PathVariable String id,
            Authentication authentication) {
        boolean isDeleted = threadService.deleteThread(id, authentication);
        return isDeleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
