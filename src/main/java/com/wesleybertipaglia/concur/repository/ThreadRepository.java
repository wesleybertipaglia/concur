package com.wesleybertipaglia.concur.repository;

import com.wesleybertipaglia.concur.model.Thread;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThreadRepository extends MongoRepository<Thread, String> {
    Page<Thread> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}