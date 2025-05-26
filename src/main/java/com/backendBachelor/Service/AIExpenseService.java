package com.backendBachelor.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
public class AIExpenseService {

    @Value("${openai.api.key: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}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIExpenseResult parseExpenseWithAI(String message, String userCurrency) {
        try {
            String prompt = createPrompt(message, userCurrency);
            String response = callOpenAI(prompt);
            return parseAIResponse(response);
        } catch (Exception e) {
            return new AIExpenseResult(
                    null, null, null, "Other", null, 0.0f,
                    "Error: " + e.getMessage()
            );
        }
    }

    private String createPrompt(String message, String userCurrency) {
        return String.format("""
            Parse this expense message and extract information in JSON format:
            - amount (number): The monetary amount spent
            - currency (string): Currency code (USD, EUR, RON, etc.) - default to %s if not specified
            - merchant (string): Where money was spent (store name, restaurant, etc.)
            - category (string): One of: Food, Transportation, Shopping, Entertainment, Healthcare, Utilities, Housing, Other
            - description (string): Brief description of the expense
            - confidence (number): Confidence level from 0.0 to 1.0

            Categories guide:
            - Food: restaurants, groceries, cafes, food delivery
            - Transportation: gas, taxi, bus, train, parking
            - Shopping: clothes, electronics, general purchases
            - Entertainment: movies, games, concerts, events
            - Healthcare: pharmacy, doctor, medical expenses
            - Utilities: bills, internet, phone, subscriptions
            - Housing: rent, furniture, home maintenance

            User message: "%s"

            Respond only with valid JSON:
            {
                "amount": 50.0,
                "currency": "USD",
                "merchant": "McDonald's",
                "category": "Food",
                "description": "Lunch at McDonald's",
                "confidence": 0.9
            }
            """, userCurrency, message);
    }

    private String callOpenAI(String prompt) throws Exception {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("max_tokens", 200);
        requestBody.put("temperature", 0.1);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new Exception("OpenAI API call failed: " + response.getStatusCode());
        }
    }

    private AIExpenseResult parseAIResponse(String apiResponse) {
        try {
            JsonNode jsonResponse = objectMapper.readTree(apiResponse);
            JsonNode choices = jsonResponse.get("choices");
            JsonNode firstChoice = choices.get(0);
            JsonNode message = firstChoice.get("message");
            String content = message.get("content").asText().trim();

            // Parse the JSON content
            JsonNode expenseJson = objectMapper.readTree(content);

            return new AIExpenseResult(
                    expenseJson.has("amount") ? expenseJson.get("amount").asDouble() : null,
                    expenseJson.has("currency") ? expenseJson.get("currency").asText() : null,
                    expenseJson.has("merchant") ? expenseJson.get("merchant").asText() : null,
                    expenseJson.has("category") ? expenseJson.get("category").asText() : "Other",
                    expenseJson.has("description") ? expenseJson.get("description").asText() : null,
                    expenseJson.has("confidence") ? expenseJson.get("confidence").floatValue() : 0.0f,
                    content
            );
        } catch (Exception e) {
            return new AIExpenseResult(
                    null, null, null, "Other", null, 0.0f,
                    "Parse error: " + e.getMessage()
            );
        }
    }

    public static class AIExpenseResult {
        private final Double amount;
        private final String currency;
        private final String merchant;
        private final String category;
        private final String description;
        private final Float confidence;
        private final String rawResponse;

        public AIExpenseResult(Double amount, String currency, String merchant, String category,
                               String description, Float confidence, String rawResponse) {
            this.amount = amount;
            this.currency = currency;
            this.merchant = merchant;
            this.category = category;
            this.description = description;
            this.confidence = confidence;
            this.rawResponse = rawResponse;
        }

        // Getters
        public Double getAmount() { return amount; }
        public String getCurrency() { return currency; }
        public String getMerchant() { return merchant; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public Float getConfidence() { return confidence; }
        public String getRawResponse() { return rawResponse; }
    }
}