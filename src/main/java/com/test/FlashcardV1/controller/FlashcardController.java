package com.test.FlashcardV1.controller;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flashcards")
@CrossOrigin(origins = "*")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @PostMapping
    public Flashcard createFlashcard(@RequestBody Flashcard flashcard) {
        return flashcardService.addFlashcard(flashcard);
    }

    @GetMapping
    public List<Flashcard> getFlashcards() {
        return flashcardService.getAllFlashcards();
    }

    @PostMapping("/{id}/review")
    public String markAsReviewed(@PathVariable Long id) {
        flashcardService.incrementReviewCount(id);
        return "Review count updated successfully!";
    }

    @GetMapping("/analytics")
    public List<Flashcard> getAnalytics() {
        return flashcardService.getReviewAnalytics();
    }

    @DeleteMapping
    public String clearAllFlashcards() {
        flashcardService.deleteAllFlashcards();
        return "All flashcards deleted successfully!";
    }
}
