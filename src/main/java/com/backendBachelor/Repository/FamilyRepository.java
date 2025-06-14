package com.backendBachelor.Repository;

import com.backendBachelor.Entity.Family;
import com.backendBachelor.Entity.FamilyInvitation;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class FamilyRepository {
    private static final String COLLECTION_FAMILIES = "families";
    private static final String COLLECTION_INVITATIONS = "familyInvitations";
    private final Firestore firestore;

    public FamilyRepository() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Family CRUD operations
    public String addFamily(Family family) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = firestore.collection(COLLECTION_FAMILIES).add(family);
        DocumentReference documentReference = future.get();
        String id = documentReference.getId();
        family.setId(id);
        updateFamily(family); // Update with the newly generated ID
        return id;
    }

    public void updateFamily(Family family) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_FAMILIES)
                .document(family.getId()).set(family);
        future.get();
    }

    public Family getFamilyById(String familyId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_FAMILIES).document(familyId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Family.class);
        }
        return null;
    }

    public Family getFamilyByUserId(String userId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_FAMILIES)
                .whereArrayContains("members", userId)
                .limit(1)
                .get();

        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        if (!documents.isEmpty()) {
            return documents.get(0).toObject(Family.class);
        }
        return null;
    }

    public void deleteFamily(String familyId) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_FAMILIES)
                .document(familyId).delete();
        future.get();
    }

    // Family Invitation CRUD operations
    public String addFamilyInvitation(FamilyInvitation invitation) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = firestore.collection(COLLECTION_INVITATIONS).add(invitation);
        DocumentReference documentReference = future.get();
        String id = documentReference.getId();
        invitation.setId(id);
        updateFamilyInvitation(invitation); // Update with the newly generated ID
        return id;
    }

    public void updateFamilyInvitation(FamilyInvitation invitation) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_INVITATIONS)
                .document(invitation.getId()).set(invitation);
        future.get();
    }

    public FamilyInvitation getFamilyInvitationById(String invitationId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_INVITATIONS).document(invitationId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(FamilyInvitation.class);
        }
        return null;
    }

    public List<FamilyInvitation> getPendingInvitationsByEmail(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_INVITATIONS)
                .whereEqualTo("invitedEmail", email)
                .whereEqualTo("status", FamilyInvitation.InvitationStatus.PENDING)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.toObjects(FamilyInvitation.class);
    }

    public List<FamilyInvitation> getInvitationsByFamily(String familyId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_INVITATIONS)
                .whereEqualTo("familyId", familyId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.toObjects(FamilyInvitation.class);
    }

    public List<FamilyInvitation> getInvitationsByUser(String userId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_INVITATIONS)
                .whereEqualTo("invitedUserId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.toObjects(FamilyInvitation.class);
    }

    public void deleteFamilyInvitation(String invitationId) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_INVITATIONS)
                .document(invitationId).delete();
        future.get();
    }

    // Check if email has pending invitation for family
    public boolean hasActiveFamilyInvitation(String familyId, String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_INVITATIONS)
                .whereEqualTo("familyId", familyId)
                .whereEqualTo("invitedEmail", email)
                .whereEqualTo("status", FamilyInvitation.InvitationStatus.PENDING)
                .limit(1)
                .get();

        QuerySnapshot querySnapshot = future.get();
        return !querySnapshot.isEmpty();
    }

    // Update family field
    public void updateFamilyField(String familyId, String field, Object value) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_FAMILIES).document(familyId);
        ApiFuture<WriteResult> future = docRef.update(field, value);
        future.get();
    }

    // Batch operations for adding/removing members
    public void addMemberToFamily(String familyId, String userId) throws ExecutionException, InterruptedException {
        Family family = getFamilyById(familyId);
        if (family != null) {
            family.addMember(userId);
            updateFamily(family);
        }
    }

    public void removeMemberFromFamily(String familyId, String userId) throws ExecutionException, InterruptedException {
        Family family = getFamilyById(familyId);
        if (family != null) {
            family.removeMember(userId);
            updateFamily(family);
        }
    }
}