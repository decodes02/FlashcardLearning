package com.test.FlashcardV1.service;

import com.test.FlashcardV1.model.UserProgress;
import com.test.FlashcardV1.repository.UserProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProgressService {
    private final UserProgressRepository repository;

    public UserProgressService(UserProgressRepository repository) {
        this.repository = repository;
    }

    public List<UserProgress> getAllUserProgress() {
        return repository.findAll();
    }

    public UserProgress logUserProgress(UserProgress progress) {
        return repository.save(progress);
    }
}
