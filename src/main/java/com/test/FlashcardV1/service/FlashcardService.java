package com.test.FlashcardV1.service;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Flashcard createFlashcard(Flashcard flashcard) {
        return repository.save(flashcard);
    }

    public Flashcard updateReviewCount(Long id) {
        return repository.findById(id).map(flashcard -> {
            flashcard.setReviewCount(flashcard.getReviewCount() + 1);
            return repository.save(flashcard);
        }).orElse(null);
    }

    public List<Map<String, Object>> getFlashcardAnalytics() {
        List<Flashcard> flashcards = repository.findAll();

        return flashcards.stream()
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("question", f.getQuestion());
                    map.put("review_count", f.getReviewCount());
                    return map;
                })
                .sorted(Comparator.comparingInt(a -> (Integer) ((Map<String, Object>) a).get("review_count")))
                .collect(Collectors.toList());
    }

}
