package com.thiago.knnchatbotapi.services;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;
    private final String model;

    public GeminiService (
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.model}") String model
    ) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();

        this.model = model;
    }

    public String answer(String question, String context) {
        String prompt = """
                Você é um assistente educacional sobre computação.

                Responda somente usando o contexto fornecido.
                Não invente informações que não estejam no contexto.
                Se o contexto não for suficiente para responder, diga que não sabe.

                CONTEXTO:
                %s

                PERGUNTA DO USUÁRIO:
                %s
                """.formatted(context, question);

        GenerateContentConfig config = GenerateContentConfig.builder()
                .maxOutputTokens(250)
                .build();

        GenerateContentResponse response = client.models.generateContent(
                model,
                prompt,
                config
        );

        return response.text();
    }
}
