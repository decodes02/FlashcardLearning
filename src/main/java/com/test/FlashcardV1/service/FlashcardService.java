package com.test.FlashcardV1.service;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    public Flashcard addFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    public List<Flashcard> getAllFlashcards() {
        return flashcardRepository.findAll();
    }

    public Optional<Flashcard> getFlashcardById(Long id) {
        return flashcardRepository.findById(id);
    }

    public void deleteAllFlashcards() {
        flashcardRepository.deleteAll();
    }

    public List<Flashcard> getReviewAnalytics() {
        return flashcardRepository.findAllByOrderByReviewCountDesc();
    }

    public void incrementReviewCount(Long id) {
        flashcardRepository.findById(id).ifPresent(flashcard -> {
            flashcard.setReviewCount(flashcard.getReviewCount() + 1);
            flashcardRepository.save(flashcard);
        });
    }
}
