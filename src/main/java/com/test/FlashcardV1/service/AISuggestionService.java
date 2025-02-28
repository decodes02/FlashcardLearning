package com.test.FlashcardV1.service;

import com.test.FlashcardV1.model.Flashcard;
import com.test.FlashcardV1.repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AISuggestionService {
    private final FlashcardRepository repository;

    public AISuggestionService(FlashcardRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> getAISuggestions() {
        List<Flashcard> flashcards = repository.findAll();
        if (flashcards.size() < 2) {
            return Collections.singletonList(Map.of("message", "Not enough flashcards for AI suggestions"));
        }

        // Get all questions
        List<String> questions = flashcards.stream()
                .map(Flashcard::getQuestion)
                .collect(Collectors.toList());

        // Compute TF-IDF similarity
        double[][] similarityMatrix = computeTFIDFSimilarity(questions);

        List<Map<String, Object>> suggestions = new ArrayList<>();

        for (int i = 0; i < flashcards.size(); i++) {
            // Get top similar indices safely
            List<Integer> topIndices = getTopSimilarIndices(similarityMatrix[i], i, flashcards.size());

            List<String> suggestedQuestions = new ArrayList<>();
            for (int index : topIndices) {
                if (index >= 0 && index < flashcards.size()) { // ✅ Ensure valid index
                    suggestedQuestions.add(flashcards.get(index).getQuestion());
                }
            }

            suggestions.add(Map.of(
                    "question", flashcards.get(i).getQuestion(),
                    "suggested", suggestedQuestions
            ));
        }

        return suggestions;
    }

    // ✅ Method to compute TF-IDF similarity
    private double[][] computeTFIDFSimilarity(List<String> questions) {
        int n = questions.size();
        double[][] similarityMatrix = new double[n][n];

        // Tokenization and TF-IDF calculation
        Map<String, double[]> tfidfVectors = computeTFIDFVectors(questions);

        // Compute cosine similarity
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                similarityMatrix[i][j] = cosineSimilarity(tfidfVectors.get(questions.get(i)), tfidfVectors.get(questions.get(j)));
            }
        }
        return similarityMatrix;
    }

    // ✅ Compute TF-IDF Vectors efficiently
    private Map<String, double[]> computeTFIDFVectors(List<String> questions) {
        Map<String, double[]> tfidfVectors = new HashMap<>();
        Set<String> allWords = new HashSet<>();

        for (String q : questions) {
            allWords.addAll(Arrays.asList(q.toLowerCase().split("\\s+")));
        }

        List<String> vocabulary = new ArrayList<>(allWords);
        int vocabSize = vocabulary.size();

        for (String q : questions) {
            double[] vector = new double[vocabSize];
            String[] words = q.toLowerCase().split("\\s+");

            for (String word : words) {
                int index = vocabulary.indexOf(word);
                if (index != -1) {
                    vector[index] += 1;
                }
            }
            tfidfVectors.put(q, vector);
        }

        return tfidfVectors;
    }

    // ✅ Compute Cosine Similarity
    private double cosineSimilarity(double[] vec1, double[] vec2) {
        double dotProduct = 0.0;
        double normVec1 = 0.0;
        double normVec2 = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normVec1 += Math.pow(vec1[i], 2);
            normVec2 += Math.pow(vec2[i], 2);
        }

        return dotProduct / (Math.sqrt(normVec1) * Math.sqrt(normVec2) + 1e-10); // ✅ Avoid division by zero
    }

    // ✅ Get top similar flashcards safely
    private List<Integer> getTopSimilarIndices(double[] similarities, int selfIndex, int size) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < similarities.length; i++) {
            if (i != selfIndex) { // ✅ Exclude itself
                indices.add(i);
            }
        }

        // Sort indices by similarity score (descending order)
        indices.sort((a, b) -> Double.compare(similarities[b], similarities[a]));

        // ✅ Ensure at least 2 suggestions
        while (indices.size() < 2) {
            indices.add(selfIndex); // Add self-index as fallback
        }

        return indices.subList(0, Math.min(2, indices.size()));
    }
}
