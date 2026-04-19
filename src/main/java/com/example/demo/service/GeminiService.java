package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateWebsite(String prompt) {
        try {
            // STEP 1: Build prompt
            String fullPrompt =
                    "You are an expert frontend developer.\n\n" +
                    "Rules:\n" +
                    "- Generate modern UI\n" +
                    "- Use Tailwind CSS\n" +
                    "- Fully responsive\n" +
                    "- Output ONLY code (no explanation)\n\n" +
                    "User request:\n" +
                    prompt;

            // Escape quotes for JSON safety
            fullPrompt = fullPrompt.replace("\"", "\\\"");

            // STEP 2: Build request body (safe for mobile)
            String requestBody = "{"
                    + "\"contents\": [{"
                    + "\"parts\": [{"
                    + "\"text\": \"" + fullPrompt + "\""
                    + "}]"
                    + "}]"
                    + "}";

            // STEP 3: Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // STEP 4: Send request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // STEP 5: Parse response JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            String result = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            // STEP 6: Clean markdown (optional but useful)
            result = result.replaceAll("```html", "")
                           .replaceAll("```", "");

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating AI response";
        }
    }
}
