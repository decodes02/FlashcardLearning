package com.test.FlashcardV1.service;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.repository.FlashcardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlashcardService {
    private final FlashcardRepository repository;

    public FlashcardService(FlashcardRepository repository) {
        this.repository = repository;
    }

    public List<Flashcard> getAllFlashcards() {
        return repository.findAll();
    }

    // ✅ Single Flashcard Creation
    public Flashcard createFlashcard(Flashcard flashcard) {
        return repository.save(flashcard);
    }

    // ✅ Multiple Flashcard Creation
    public List<Flashcard> createMultipleFlashcards(List<Flashcard> flashcards) {
        return repository.saveAll(flashcards);
    }

    // ✅ Update Review Count
    public Map<String, Object> updateReviewCount(Long id) {
        Flashcard flashcard = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flashcard not found"));

        flashcard.setReviewCount(flashcard.getReviewCount() + 1);
        repository.save(flashcard);

        return Map.of(
                "message", "Review updated successfully",
                "review_count", flashcard.getReviewCount()
        );
    }

    // ✅ Flashcard Analytics (Sorted by review count DESC)
    public List<Map<String, Object>> getFlashcardAnalytics() {
        List<Flashcard> flashcards = repository.findAll();

        return flashcards.stream()
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("question", f.getQuestion());
                    map.put("review_count", f.getReviewCount());
                    return map;
                })
                .sorted(Comparator.comparingInt(m -> (Integer) ((Map<String, Object>) m).get("review_count")).reversed()) // ✅ Explicit Casting
                .collect(Collectors.toList());
    }


}
