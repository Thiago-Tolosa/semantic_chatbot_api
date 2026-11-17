package com.thiago.knnchatbotapi.services;

import com.google.genai.errors.ServerException;
import com.thiago.knnchatbotapi.data.ChatDataset;
import com.thiago.knnchatbotapi.models.SimilarityResult;
import com.thiago.knnchatbotapi.models.TrainingExample;
import com.thiago.knnchatbotapi.models.TrainingExampleVectorized;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatServices {

    private final VectorizerService vectorizer;
    private final List<TrainingExampleVectorized> vectorizedExamples;
    private final KnnService knnService = new KnnService();
    private final GeminiService geminiService;

    public String answer(String message) {
        double[] questionVector = vectorizer.vectorize(message);

        List<SimilarityResult> matches = knnService.findNearest(
                questionVector,
                vectorizedExamples,
                5
        );

        SimilarityResult bestMatch = matches.getFirst();

        if (bestMatch.similarity() < 0.35) {
            return "Não encontrei uma resposta confiável para essa pergunta. "
                    + "Posso ajudar com temas de estruturas de dados, algoritmos e Java.";
        }

        String context = matches.stream()
                .map(match -> """
                Pergunta: %s
                Resposta: %s
                """.formatted(
                        match.example().example().question(),
                        match.example().example().answer()
                ))
                .collect(java.util.stream.Collectors.joining("\n---\n"));

        try {
            return geminiService.answer(message, context);
        } catch (ServerException exception) {
            return bestMatch.example().example().answer();
        }
    }

    public ChatServices( GeminiService geminiService ) {
        this.geminiService = geminiService;
        ChatDataset dataset = new ChatDataset();

        TextNormalizer normalizer = new TextNormalizer();
        this.vectorizer = new VectorizerService(normalizer);

        List<String> questions = dataset.getData()
                .stream()
                .map(TrainingExample::question)
                .toList();

        vectorizer.buildVocabulary(questions);

        this.vectorizedExamples = dataset.getData()
                .stream()
                .map(example -> new TrainingExampleVectorized(
                        example,
                        vectorizer.vectorize(example.question())
                ))
                .toList();
    }
}
