package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello Spring Boot!");
        return response;
    }
    @PostMapping("/generate")
    @CrossOrigin(origins = "*")
    public Map<String, String> generate(@RequestBody Map<String, String> body) {

        String prompt = body.get("prompt");
        Map<String, String> response = new HashMap<>();
        System.out.println("prompt: " + prompt);

        response.put("html", "<h1>Hello AI Website</h1>");
        response.put("css", "body { background:black; color:white; text-align:center; }");
        return response;
    }
}