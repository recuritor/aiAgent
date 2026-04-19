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
        try{
            String fullprompt = """
            You are an expert frontend developer.
            
            Rules:
            - Generate modern UI
            - Use Tailwind CSS
            - Fully responsive
            - Output ONLY code (no explanation)
            
            User request:
            """ + prompt;
            
            String requestBody = """
            {
                "contents": [{
                "parts": [{
                "text": "%s"
                    }]
                }]
            }
                          
            """.formatted(fullPrompt);

            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(
                                                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey))
                                             .header("Content-Type", "application/json")
                                             .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                             .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            return root
                     .path("candidates")
                     .get(0)
                     .path("content").path("parts").get(0).path("text").asText();
                                                                                                                                                          
                                                                                                                                                                                                                                                                                                         
        }
        catch(Exception e) {
            e.printStackTrace();
            return "error in gemini api service";
        }

    }
}