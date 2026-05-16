package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateWebsite(String prompt) {
        try {
                String fullPrompt =
                        "You are an expert frontend developer.\n\n" +
                        "Rules:\n" +
                        "- Generate modern UI\n" +
                        "- Use Tailwind CSS\n" +
                        "- Fully responsive\n" +
                        "- Output ONLY code (no explanation)\n\n" +
                        "User request:\n" +
                        prompt;
                
                Client client = new Client();

                GenerateContentResponse response =
                        client.models.generateContent(
                        "gemini-3-flash-preview",
                        fullPrompt,
                        null);
                
                String result = response.text();

                if (result != null) {
                result = result.replaceAll("```html", "")
                                .replaceAll("```", "");
                }

                return result != null ? result : "No response generated";
        } 
        catch (Exception e) {
            e.printStackTrace();
            return "Error generating AI response";
        }
    }
}
