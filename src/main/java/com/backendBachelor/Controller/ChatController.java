package com.backendBachelor.Controller;

import com.backendBachelor.Dto.ExpenseDto;
import com.backendBachelor.Service.ChatExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatExpenseService chatExpenseService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Chat service is running");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test successful!");
        response.put("received", request.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parse")
    public ResponseEntity<ParseExpenseResponse> parseExpenseMessage(@RequestBody ParseExpenseRequest request) {
        try {
            System.out.println("Received parse request: " + request.getMessage());

            ParseExpenseResponse response = chatExpenseService.parseExpenseMessage(
                    request.getMessage(),
                    request.getUserId(),
                    request.getUserCurrency()
            );

            System.out.println("Sending response: " + response.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in parseExpenseMessage: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.badRequest().body(
                    new ParseExpenseResponse(false, "Error: " + e.getMessage(), null, null, null)
            );
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmExpenseResponse> confirmExpense(@RequestBody ConfirmExpenseRequest request) {
        try {
            String expenseId = chatExpenseService.confirmExpense(request.getExpenseDto());
            return ResponseEntity.ok(new ConfirmExpenseResponse(true, "Expense saved successfully", expenseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ConfirmExpenseResponse(false, "Failed to save expense: " + e.getMessage(), null)
            );
        }
    }

    @PostMapping("/suggestions")
    public ResponseEntity<SuggestionsResponse> getSuggestions(@RequestBody SuggestionsRequest request) {
        try {
            SuggestionsResponse response = chatExpenseService.getSuggestions(
                    request.getMessage(),
                    request.getUserId()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new SuggestionsResponse(null, "Error getting suggestions: " + e.getMessage())
            );
        }
    }

    public static class ParseExpenseRequest {
        private String message;
        private String userId;
        private String userCurrency;

        public ParseExpenseRequest() {}

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUserCurrency() { return userCurrency; }
        public void setUserCurrency(String userCurrency) { this.userCurrency = userCurrency; }
    }

    public static class ParseExpenseResponse {
        private boolean success;
        private String message;
        private ExpenseDto expenseDto;
        private Double confidence;
        private Map<String, Object> extractedData;

        public ParseExpenseResponse() {}

        public ParseExpenseResponse(boolean success, String message, ExpenseDto expenseDto,
                                    Double confidence, Map<String, Object> extractedData) {
            this.success = success;
            this.message = message;
            this.expenseDto = expenseDto;
            this.confidence = confidence;
            this.extractedData = extractedData;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public ExpenseDto getExpenseDto() { return expenseDto; }
        public void setExpenseDto(ExpenseDto expenseDto) { this.expenseDto = expenseDto; }

        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }

        public Map<String, Object> getExtractedData() { return extractedData; }
        public void setExtractedData(Map<String, Object> extractedData) { this.extractedData = extractedData; }
    }

    public static class ConfirmExpenseRequest {
        private ExpenseDto expenseDto;

        public ConfirmExpenseRequest() {}
        public ExpenseDto getExpenseDto() { return expenseDto; }
        public void setExpenseDto(ExpenseDto expenseDto) { this.expenseDto = expenseDto; }
    }

    public static class ConfirmExpenseResponse {
        private boolean success;
        private String message;
        private String expenseId;

        public ConfirmExpenseResponse() {}

        public ConfirmExpenseResponse(boolean success, String message, String expenseId) {
            this.success = success;
            this.message = message;
            this.expenseId = expenseId;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getExpenseId() { return expenseId; }
        public void setExpenseId(String expenseId) { this.expenseId = expenseId; }
    }

    public static class SuggestionsRequest {
        private String message;
        private String userId;

        public SuggestionsRequest() {}

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }

    public static class SuggestionsResponse {
        private List<String> suggestions;
        private String error;

        public SuggestionsResponse() {}

        public SuggestionsResponse(List<String> suggestions, String error) {
            this.suggestions = suggestions;
            this.error = error;
        }

        public List<String> getSuggestions() { return suggestions; }
        public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}