package com.thiago.knnchatbotapi.services;
import com.thiago.knnchatbotapi.models.SimilarityResult;
import com.thiago.knnchatbotapi.models.TrainingExampleVectorized;

import java.util.Comparator;
import java.util.List;

public class KnnService {

    public double cosineSimilarity(double[] first, double[] second){
        double dotProduct = 0;
        double firstMagnitude = 0;
        double secondMagnitude = 0;

        for( int i = 0; i < first.length; i++ ){
            dotProduct += first[i] * second[i];
            firstMagnitude += first[i] * first[i];
            secondMagnitude += second[i] * second[i];
        }

        if ( firstMagnitude == 0 || secondMagnitude == 0 ){
            return 0;
        }

        return dotProduct / (
                Math.sqrt(firstMagnitude) * Math.sqrt(secondMagnitude)
        );
    }

    public List<SimilarityResult> findNearest(
            double[] questionVector,
            List<TrainingExampleVectorized> examples,
            int k
    ) {
        if (k <= 0) {
            throw new IllegalArgumentException("k deve ser maior que zero.");
        }

        return examples.stream()
                .map(item -> new SimilarityResult(
                        item,
                        cosineSimilarity(questionVector, item.vector())
                ))
                .sorted(Comparator.comparingDouble(
                        SimilarityResult::similarity
                ).reversed())
                .limit(k)
                .toList();
    }
}
