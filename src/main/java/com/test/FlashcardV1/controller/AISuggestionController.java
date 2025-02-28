package com.test.FlashcardV1.controller;

import com.test.FlashcardV1.service.AISuggestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai-suggestions")
@CrossOrigin(origins = "http://localhost:5500")
public class AISuggestionController {
    private final AISuggestionService service;

    public AISuggestionController(AISuggestionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Map<String, Object>> getAISuggestions() {
        return service.getAISuggestions();
    }
}
