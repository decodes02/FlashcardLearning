package com.test.FlashcardV1.controller;

import com.test.FlashcardV1.model.UserProgress;
import com.test.FlashcardV1.service.UserProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-progress")
@CrossOrigin(origins = "http://localhost:5500")
public class UserProgressController {
    private final UserProgressService service;

    public UserProgressController(UserProgressService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserProgress> getAllUserProgress() {
        return service.getAllUserProgress();
    }

    @PostMapping
    public UserProgress logUserProgress(@RequestBody UserProgress progress) {
        return service.logUserProgress(progress);
    }
}
