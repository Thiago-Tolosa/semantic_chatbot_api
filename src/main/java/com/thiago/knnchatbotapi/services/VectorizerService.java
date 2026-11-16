package com.thiago.knnchatbotapi.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VectorizerService {

    private final TextNormalizer textNormalizer;
    private final Map<String, Integer> vocabularyIndex = new HashMap<>();

    public VectorizerService(TextNormalizer textNormalizer){
        this.textNormalizer = textNormalizer;
    }

    public void buildVocabulary(List<String> questions){
        for (String question : questions) {
            List<String> tokens = textNormalizer.tokenize(question);

            for (String token : tokens) {
                vocabularyIndex.putIfAbsent(token, vocabularyIndex.size());
            }
        }
    }

    public double[] vectorize(String text) {
        double[] vector = new double[vocabularyIndex.size()];

        List<String> tokens = textNormalizer.tokenize(text);

        for (String token : tokens) {
            Integer index = vocabularyIndex.get(token);

            if(index != null){
                vector[index]++;
            }
        }

        return vector;
    }
}
