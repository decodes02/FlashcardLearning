package com.test.FlashcardV1.controller;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.service.FlashcardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flashcards")
@CrossOrigin(origins = "http://localhost:5500")
public class FlashcardController {
    private final FlashcardService service;

    public FlashcardController(FlashcardService service) {
        this.service = service;
    }

    @GetMapping
    public List<Flashcard> getAllFlashcards() {
        List<Flashcard> flashcards = service.getAllFlashcards();
        if (flashcards.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No flashcards found");
        }
        return flashcards;
    }

    @GetMapping("/analytics")
    public List<Map<String, Object>> getFlashcardAnalytics() {
        return service.getFlashcardAnalytics();
    }


    @PostMapping
    public Flashcard createFlashcard(@RequestBody Flashcard flashcard) {
        return service.createFlashcard(flashcard);
    }

    @PostMapping("/review/{id}")
    public Flashcard updateReviewCount(@PathVariable Long id) {
        return service.updateReviewCount(id);
    }
}
