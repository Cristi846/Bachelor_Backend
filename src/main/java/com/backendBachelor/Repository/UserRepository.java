package com.backendBachelor.Repository;

import com.backendBachelor.Entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    private static final String COLLECTION_USERS = "users";
    private final Firestore firestore;

    public UserRepository() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public String addUser(User user) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = firestore.collection(COLLECTION_USERS).add(user);
        DocumentReference documentReference = future.get();
        String id = documentReference.getId();
        user.setId(id);
        updateUser(user);
        return id;
    }

    public void updateUser(User user) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_USERS).document(user.getId()).set(user);
        future.get();
    }

    public User getUserById(String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_USERS).document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(User.class);
        }
        return null;
    }

    public void updateUserField(String userId, String field, Object value) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_USERS).document(userId);
        ApiFuture<WriteResult> future = docRef.update(field, value);
        future.get();
    }

    public void updateUserFields(String userId, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_USERS).document(userId);
        ApiFuture<WriteResult> future = docRef.update(updates);
        future.get();
    }

    public void updateCategoryBudget(String userId, String category, double amount) throws ExecutionException, InterruptedException {
        User user = getUserById(userId);
        if (user != null) {
            Map<String, Double> budgets = user.getCategoryBudgets();
            budgets.put(category, amount);

            Map<String, Object> updates = new HashMap<>();
            updates.put("categoryBudgets", budgets);

            updateUserFields(userId, updates);
        }
    }

    public void deleteUser(String userId) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_USERS).document(userId).delete();
        future.get();
    }
}