package com.test.FlashcardV1.controller;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // ✅ Get All Flashcards
    @GetMapping
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
        List<Flashcard> flashcards = service.getAllFlashcards();
        if (flashcards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(flashcards);
    }

    // ✅ Get Flashcard Analytics (Sorted DESC)
    @GetMapping("/analytics")
    public ResponseEntity<List<Map<String, Object>>> getFlashcardAnalytics() {
        return ResponseEntity.ok(service.getFlashcardAnalytics());
    }

    // ✅ Add Single Flashcard
    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody Flashcard flashcard) {
        return ResponseEntity.ok(service.createFlashcard(flashcard));
    }

    // ✅ Add Multiple Flashcards
    @PostMapping("/bulk")
    public ResponseEntity<List<Flashcard>> createMultipleFlashcards(@RequestBody List<Flashcard> flashcards) {
        return ResponseEntity.ok(service.createMultipleFlashcards(flashcards));
    }

    // ✅ Update Review Count
    @PostMapping("/review/{id}")
    public ResponseEntity<Map<String, Object>> updateReviewCount(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateReviewCount(id));
    }
}
