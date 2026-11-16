package com.thiago.knnchatbotapi.services;

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

    public String answer(String message) {
        double[] questionVector = vectorizer.vectorize(message);

        List<SimilarityResult> matches = knnService.findNearest(
                questionVector,
                vectorizedExamples,
                1
        );

        SimilarityResult bestMatch = matches.getFirst();

        if (bestMatch.similarity() < 0.35) {
            return "Não encontrei uma resposta confiável para essa pergunta. "
                    + "Posso ajudar com temas de estruturas de dados, algoritmos e Java.";
        }

        return bestMatch.example().example().answer();
    }

    public ChatServices() {
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
