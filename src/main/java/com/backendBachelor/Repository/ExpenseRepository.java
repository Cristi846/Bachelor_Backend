package com.backendBachelor.Repository;

import com.backendBachelor.Entity.Expense;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExpenseRepository {
    private static final String COLLECTION_EXPENSES = "expenses";
    private final Firestore firestore;

    public ExpenseRepository() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public String addExpense(Expense expense) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = firestore.collection(COLLECTION_EXPENSES).add(expense);
        DocumentReference documentReference = future.get();
        return documentReference.getId();
    }

    public void updateExpense(Expense expense) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_EXPENSES).document(expense.getId()).set(expense);
        future.get(); // Wait for the operation to complete
    }

    public void deleteExpense(String expenseId) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_EXPENSES).document(expenseId).delete();
        future.get(); // Wait for the operation to complete
    }

    public List<Expense> getExpensesByUser(String userId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_EXPENSES)
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.toObjects(Expense.class);
    }

    public List<Expense> getExpensesByCategory(String userId, String category) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_EXPENSES)
                .whereEqualTo("userId", userId)
                .whereEqualTo("category", category)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.toObjects(Expense.class);
    }

    public List<Expense> getExpensesByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate)
            throws ExecutionException, InterruptedException {
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_EXPENSES)
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("timestamp", start)
                .whereLessThanOrEqualTo("timestamp", end)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.toObjects(Expense.class);
    }
}