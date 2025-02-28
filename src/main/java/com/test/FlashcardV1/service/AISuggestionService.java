package com.test.FlashcardV1.service;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AISuggestionService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    public List<Map<String, Object>> generateAISuggestions() {
        List<Flashcard> flashcards = flashcardRepository.findAll();
        List<String> questions = new ArrayList<>();
        flashcards.forEach(card -> questions.add(card.getQuestion()));

        if (questions.size() < 2) {
            return Collections.singletonList(Map.of("message", "Not enough flashcards for AI suggestions"));
        }

        Map<String, Double> tfidfScores = computeTFIDF(questions);
        List<Map<String, Object>> suggestions = new ArrayList<>();

        for (String question : questions) {
            List<String> similarQuestions = getTopSimilarQuestions(tfidfScores, question, questions);
            suggestions.add(Map.of("question", question, "suggested", similarQuestions));
        }

        return suggestions;
    }

    private Map<String, Double> computeTFIDF(List<String> questions) {
        Map<String, Double> tfidfScores = new HashMap<>();
        Map<String, Integer> termFrequencies = new HashMap<>();
        Map<String, Integer> documentFrequencies = new HashMap<>();

        int totalDocs = questions.size();

        for (String question : questions) {
            String[] words = question.toLowerCase().split("\\s+");
            Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));

            for (String word : words) {
                termFrequencies.put(word, termFrequencies.getOrDefault(word, 0) + 1);
            }

            for (String word : uniqueWords) {
                documentFrequencies.put(word, documentFrequencies.getOrDefault(word, 0) + 1);
            }
        }

        for (String term : termFrequencies.keySet()) {
            double tf = (double) termFrequencies.get(term) / totalDocs;
            double idf = Math.log((double) totalDocs / (1 + documentFrequencies.get(term)));
            tfidfScores.put(term, tf * idf);
        }

        return tfidfScores;
    }

    private List<String> getTopSimilarQuestions(Map<String, Double> tfidfScores, String question, List<String> questions) {
        Map<String, Double> similarityScores = new HashMap<>();

        for (String otherQuestion : questions) {
            if (!otherQuestion.equals(question)) {
                double similarity = computeSimilarity(tfidfScores, question, otherQuestion);
                similarityScores.put(otherQuestion, similarity);
            }
        }

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .toList();
    }

    private double computeSimilarity(Map<String, Double> tfidfScores, String q1, String q2) {
        String[] words1 = q1.toLowerCase().split("\\s+");
        String[] words2 = q2.toLowerCase().split("\\s+");

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String word : words1) {
            dotProduct += tfidfScores.getOrDefault(word, 0.0) * tfidfScores.getOrDefault(word, 0.0);
            norm1 += Math.pow(tfidfScores.getOrDefault(word, 0.0), 2);
        }

        for (String word : words2) {
            norm2 += Math.pow(tfidfScores.getOrDefault(word, 0.0), 2);
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10);
    }
}
