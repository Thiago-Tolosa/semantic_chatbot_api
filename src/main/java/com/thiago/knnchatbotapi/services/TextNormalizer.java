package com.thiago.knnchatbotapi.services;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

public class TextNormalizer {
    public List<String> tokenize(String text) {

        if ( text == null || text.isBlank()){
            return List.of();
        }

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        if ( normalized.isBlank()){
            return List.of();
        }

        return Arrays.asList(normalized.split(" "));
    }
}
