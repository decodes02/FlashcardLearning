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

    // ✅ Add Single or Multiple Flashcards
    @PostMapping
    public ResponseEntity<?> createFlashcards(@RequestBody Object payload) {
        if (payload instanceof List) {
            // Handle multiple flashcards
            @SuppressWarnings("unchecked")
            List<Flashcard> flashcards = (List<Flashcard>) payload;
            return ResponseEntity.ok(service.createMultipleFlashcards(flashcards));
        } else if (payload instanceof Map) {
            // Handle single flashcard
            @SuppressWarnings("unchecked")
            Map<String, String> flashcardData = (Map<String, String>) payload;
            Flashcard flashcard = new Flashcard();
            flashcard.setQuestion(flashcardData.get("question"));
            flashcard.setAnswer(flashcardData.get("answer"));
            return ResponseEntity.ok(service.createFlashcard(flashcard));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid JSON format"));
        }
    }

    // ✅ Update Review Count
    @PostMapping("/review/{id}")
    public ResponseEntity<Map<String, Object>> updateReviewCount(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateReviewCount(id));
    }
}
