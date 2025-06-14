package com.backendBachelor.Service;

import com.backendBachelor.Controller.ChatController.*;
import com.backendBachelor.Dto.ExpenseDto;
import com.backendBachelor.Entity.User;
import com.backendBachelor.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatExpenseService {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    public ChatExpenseService(ExpenseService expenseService, UserRepository userRepository) {
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(\\d+(?:\\.\\d{1,2})?)\\s*(lei|ron|euro|euros|dollar|dollars|\\$|€)", Pattern.CASE_INSENSITIVE);
    private static final Pattern MERCHANT_PATTERN = Pattern.compile("(?:from|at|in)\\s+([A-Za-z][A-Za-z0-9\\s&'.-]{1,30})", Pattern.CASE_INSENSITIVE);

    public ParseExpenseResponse parseExpenseMessage(String message, String userId, String userCurrency) {
        try {
            ParsedExpenseData parsed = parseMessageSimple(message, userCurrency);

            if (parsed.amount != null && parsed.confidence >= 0.5) {
                ExpenseDto expenseDto = createExpenseDto(parsed, userId);

                String responseMessage = buildSimpleResponseMessage(parsed, true);

                Map<String, Object> extractedData = new HashMap<>();
                extractedData.put("amount", parsed.amount);
                extractedData.put("currency", parsed.currency);
                extractedData.put("merchant", parsed.merchant);
                extractedData.put("category", parsed.category);

                return new ParseExpenseResponse(true, responseMessage, expenseDto,
                        (double) parsed.confidence, extractedData);
            } else {
                String responseMessage = buildSimpleResponseMessage(parsed, false);

                return new ParseExpenseResponse(false, responseMessage, null,
                        (double) parsed.confidence, null);
            }
        } catch (Exception e) {
            return new ParseExpenseResponse(false, "Error parsing message: " + e.getMessage(),
                    null, 0.0, null);
        }
    }

    public String confirmExpense(ExpenseDto expenseDto) {
        try {
            return expenseService.addExpense(expenseDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save expense: " + e.getMessage(), e);
        }
    }

    public SuggestionsResponse getSuggestions(String message, String userId) {
        try {
            List<String> suggestions = new ArrayList<>();

            suggestions.add("💡 Try: \"I spent 50 euros at McDonald's\"");
            suggestions.add("💡 Try: \"Bought groceries from Auchan for 200 lei\"");
            suggestions.add("💡 Try: \"Gas station 40 dollars\"");

            return new SuggestionsResponse(suggestions, null);
        } catch (Exception e) {
            return new SuggestionsResponse(null, "Error generating suggestions: " + e.getMessage());
        }
    }

    private ParsedExpenseData parseMessageSimple(String message, String defaultCurrency) {
        String cleanMessage = message.toLowerCase();
        double confidence = 0.0;

        Double amount = null;
        String currency = defaultCurrency;
        Matcher amountMatcher = AMOUNT_PATTERN.matcher(message);
        if (amountMatcher.find()) {
            try {
                amount = Double.parseDouble(amountMatcher.group(1));
                String currencyMatch = amountMatcher.group(2).toLowerCase();
                if (currencyMatch.contains("lei") || currencyMatch.contains("ron")) {
                    currency = "RON";
                } else if (currencyMatch.contains("euro")) {
                    currency = "EUR";
                } else if (currencyMatch.contains("dollar") || currencyMatch.contains("$")) {
                    currency = "USD";
                }
                confidence += 0.6;
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        String merchant = null;
        Matcher merchantMatcher = MERCHANT_PATTERN.matcher(message);
        if (merchantMatcher.find()) {
            merchant = merchantMatcher.group(1).trim();
            confidence += 0.3;
        }

        String category = "Other";
        if (cleanMessage.contains("food") || cleanMessage.contains("restaurant") ||
                cleanMessage.contains("groceries") || cleanMessage.contains("auchan") ||
                cleanMessage.contains("mcdonald")) {
            category = "Food";
            confidence += 0.1;
        }

        return new ParsedExpenseData(amount, currency, merchant, category,
                "Simple parsed expense", confidence);
    }

    private ExpenseDto createExpenseDto(ParsedExpenseData parsed, String userId) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setUserId(userId);
        dto.setAmount(parsed.amount);
        dto.setCategory(parsed.category);
        dto.setDescription(parsed.description);
        dto.setTimestamp(LocalDateTime.now());
        return dto;
    }

    private String buildSimpleResponseMessage(ParsedExpenseData parsed, boolean highConfidence) {
        if (highConfidence) {
            StringBuilder sb = new StringBuilder("✅ Simple Parser Result:\n\n");
            sb.append("💰 Amount: ").append(parsed.amount).append(" ").append(parsed.currency).append("\n");
            if (parsed.merchant != null) {
                sb.append("🏪 Merchant: ").append(parsed.merchant).append("\n");
            }
            sb.append("📂 Category: ").append(parsed.category).append("\n\n");
            sb.append("Would you like me to add this expense?");
            return sb.toString();
        } else {
            return "❌ I couldn't parse your message well. Try: \"I spent 50 euros at McDonald's\"";
        }
    }

    private static class ParsedExpenseData {
        Double amount;
        String currency;
        String merchant;
        String category;
        String description;
        double confidence;

        ParsedExpenseData(Double amount, String currency, String merchant, String category,
                          String description, double confidence) {
            this.amount = amount;
            this.currency = currency;
            this.merchant = merchant;
            this.category = category;
            this.description = description;
            this.confidence = confidence;
        }
    }
}