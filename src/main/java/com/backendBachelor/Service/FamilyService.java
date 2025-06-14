package com.backendBachelor.Service;

import com.backendBachelor.Dto.ExpenseDto;
import com.backendBachelor.Entity.Family;
import com.backendBachelor.Entity.FamilyInvitation;
import com.backendBachelor.Entity.User;
import com.backendBachelor.Repository.FamilyRepository;
import com.backendBachelor.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final ExpenseService expenseService;

    public FamilyService() {
        this.familyRepository = new FamilyRepository();
        this.userRepository = new UserRepository();
        this.expenseService = new ExpenseService();
    }

    /**
     * Create a new family
     */
    public String createFamily(String userId, String familyName) throws ExecutionException, InterruptedException {
        // Check if user already belongs to a family
        Family existingFamily = familyRepository.getFamilyByUserId(userId);
        if (existingFamily != null) {
            throw new RuntimeException("User already belongs to a family: " + existingFamily.getName());
        }

        // Get user information
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Create new family
        Family family = new Family(familyName, userId);
        String familyId = familyRepository.addFamily(family);

        // Update user with family reference
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("familyId", familyId);
        userUpdates.put("familyRole", "admin");
        userRepository.updateUserFields(userId, userUpdates);

        return familyId;
    }

    /**
     * Invite a user to join the family
     */
    public String inviteUserToFamily(String familyId, String inviterUserId, String inviteeEmail)
            throws ExecutionException, InterruptedException {
        // Validate family and inviter permissions
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.isAdmin(inviterUserId)) {
            throw new RuntimeException("Only family admins can invite members");
        }

        // Check if email already has a pending invitation for this family
        if (familyRepository.hasActiveFamilyInvitation(familyId, inviteeEmail)) {
            throw new RuntimeException("User already has a pending invitation for this family");
        }

        // Get inviter information
        User inviter = userRepository.getUserById(inviterUserId);
        if (inviter == null) {
            throw new RuntimeException("Inviter not found");
        }

        // Create invitation
        FamilyInvitation invitation = new FamilyInvitation(
                familyId,
                family.getName(),
                inviterUserId,
                inviter.getName(),
                inviteeEmail
        );

        return familyRepository.addFamilyInvitation(invitation);
    }

    /**
     * Accept a family invitation
     */
    public void acceptFamilyInvitation(String invitationId, String userId)
            throws ExecutionException, InterruptedException {
        // Get invitation
        FamilyInvitation invitation = familyRepository.getFamilyInvitationById(invitationId);
        if (invitation == null) {
            throw new RuntimeException("Invitation not found");
        }

        if (!invitation.isPending()) {
            throw new RuntimeException("Invitation is no longer valid");
        }

        // Get user
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Check if user's email matches invitation
        if (!user.getEmail().equals(invitation.getInvitedEmail())) {
            throw new RuntimeException("User email does not match invitation");
        }

        // Check if user already belongs to a family
        Family existingFamily = familyRepository.getFamilyByUserId(userId);
        if (existingFamily != null) {
            throw new RuntimeException("User already belongs to a family");
        }

        // Accept invitation
        invitation.accept(userId);
        familyRepository.updateFamilyInvitation(invitation);

        // Add user to family
        familyRepository.addMemberToFamily(invitation.getFamilyId(), userId);

        // Update user with family reference
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("familyId", invitation.getFamilyId());
        userUpdates.put("familyRole", "member");
        userRepository.updateUserFields(userId, userUpdates);
    }

    /**
     * Decline a family invitation
     */
    public void declineFamilyInvitation(String invitationId, String userId)
            throws ExecutionException, InterruptedException {
        FamilyInvitation invitation = familyRepository.getFamilyInvitationById(invitationId);
        if (invitation == null) {
            throw new RuntimeException("Invitation not found");
        }

        if (!invitation.isPending()) {
            throw new RuntimeException("Invitation is no longer valid");
        }

        invitation.decline();
        familyRepository.updateFamilyInvitation(invitation);
    }

    /**
     * Remove a member from the family
     */
    public void removeFamilyMember(String familyId, String adminUserId, String memberUserId)
            throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.isAdmin(adminUserId)) {
            throw new RuntimeException("Only family admins can remove members");
        }

        if (!family.isMember(memberUserId)) {
            throw new RuntimeException("User is not a member of this family");
        }

        // Cannot remove the creator of the family
        if (family.getCreatedBy().equals(memberUserId)) {
            throw new RuntimeException("Cannot remove the family creator");
        }

        // Remove from family
        familyRepository.removeMemberFromFamily(familyId, memberUserId);

        // Update user to remove family reference
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("familyId", null);
        userUpdates.put("familyRole", null);
        userRepository.updateUserFields(memberUserId, userUpdates);
    }

    /**
     * Update family shared budget
     */
    public void updateSharedBudget(String familyId, String adminUserId, Family.FamilyBudget budget)
            throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.isAdmin(adminUserId)) {
            throw new RuntimeException("Only family admins can update the budget");
        }

        family.setSharedBudget(budget);
        familyRepository.updateFamily(family);
    }

    /**
     * Get family by ID
     */
    public Family getFamilyById(String familyId) throws ExecutionException, InterruptedException {
        return familyRepository.getFamilyById(familyId);
    }

    /**
     * Get family by user ID
     */
    public Family getFamilyByUserId(String userId) throws ExecutionException, InterruptedException {
        return familyRepository.getFamilyByUserId(userId);
    }

    /**
     * Get family expenses
     */
    public List<ExpenseDto> getFamilyExpenses(String familyId) throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        List<ExpenseDto> allExpenses = new ArrayList<>();

        // Get expenses for all family members
        for (String memberId : family.getMembers()) {
            List<ExpenseDto> memberExpenses = expenseService.getExpensesByUser(memberId);
            // Filter for family expenses (non-personal)
            List<ExpenseDto> familyExpenses = memberExpenses.stream()
                    .filter(expense -> !isPersonalExpense(expense))
                    .collect(Collectors.toList());
            allExpenses.addAll(familyExpenses);
        }

        // Sort by timestamp descending
        allExpenses.sort((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()));

        return allExpenses;
    }

    /**
     * Get family spending analysis
     */
    public Map<String, Double> getFamilySpendingAnalysis(String familyId)
            throws ExecutionException, InterruptedException {
        List<ExpenseDto> familyExpenses = getFamilyExpenses(familyId);

        Map<String, Double> categorySpending = new HashMap<>();

        for (ExpenseDto expense : familyExpenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            categorySpending.put(category, categorySpending.getOrDefault(category, 0.0) + amount);
        }

        return categorySpending;
    }

    /**
     * Get member spending analysis
     */
    public Map<String, Double> getMemberSpendingAnalysis(String familyId)
            throws ExecutionException, InterruptedException {
        List<ExpenseDto> familyExpenses = getFamilyExpenses(familyId);

        Map<String, Double> memberSpending = new HashMap<>();

        for (ExpenseDto expense : familyExpenses) {
            String userId = expense.getUserId();
            double amount = expense.getAmount();
            memberSpending.put(userId, memberSpending.getOrDefault(userId, 0.0) + amount);
        }

        return memberSpending;
    }

    /**
     * Get pending invitations for user email
     */
    public List<FamilyInvitation> getPendingInvitationsForUser(String email)
            throws ExecutionException, InterruptedException {
        return familyRepository.getPendingInvitationsByEmail(email);
    }

    /**
     * Get family invitations (for admins)
     */
    public List<FamilyInvitation> getFamilyInvitations(String familyId, String adminUserId)
            throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.isAdmin(adminUserId)) {
            throw new RuntimeException("Only family admins can view invitations");
        }

        return familyRepository.getInvitationsByFamily(familyId);
    }

    /**
     * Cancel family invitation
     */
    public void cancelFamilyInvitation(String invitationId, String adminUserId)
            throws ExecutionException, InterruptedException {
        FamilyInvitation invitation = familyRepository.getFamilyInvitationById(invitationId);
        if (invitation == null) {
            throw new RuntimeException("Invitation not found");
        }

        Family family = familyRepository.getFamilyById(invitation.getFamilyId());
        if (family == null || !family.isAdmin(adminUserId)) {
            throw new RuntimeException("Only family admins can cancel invitations");
        }

        invitation.cancel();
        familyRepository.updateFamilyInvitation(invitation);
    }

    /**
     * Leave family (for non-admin members)
     */
    public void leaveFamily(String familyId, String userId) throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.isMember(userId)) {
            throw new RuntimeException("User is not a member of this family");
        }

        if (family.getCreatedBy().equals(userId)) {
            throw new RuntimeException("Family creator cannot leave. Transfer ownership or delete the family.");
        }

        // Remove from family
        familyRepository.removeMemberFromFamily(familyId, userId);

        // Update user to remove family reference
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("familyId", null);
        userUpdates.put("familyRole", null);
        userRepository.updateUserFields(userId, userUpdates);
    }

    /**
     * Delete family (only creator can do this)
     */
    public void deleteFamily(String familyId, String creatorUserId) throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.getCreatedBy().equals(creatorUserId)) {
            throw new RuntimeException("Only the family creator can delete the family");
        }

        // Remove family reference from all members
        for (String memberId : family.getMembers()) {
            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("familyId", null);
            userUpdates.put("familyRole", null);
            userRepository.updateUserFields(memberId, userUpdates);
        }

        // Cancel all pending invitations
        List<FamilyInvitation> invitations = familyRepository.getInvitationsByFamily(familyId);
        for (FamilyInvitation invitation : invitations) {
            if (invitation.isPending()) {
                invitation.cancel();
                familyRepository.updateFamilyInvitation(invitation);
            }
        }

        // Delete the family
        familyRepository.deleteFamily(familyId);
    }

    /**
     * Helper method to check if expense is personal
     * This would be determined by expense metadata
     */
    private boolean isPersonalExpense(ExpenseDto expense) {
        // For now, assume all expenses are family expenses
        // In real implementation, this would check expense metadata
        return false;
    }

    /**
     * Update family settings
     */
    public void updateFamilySettings(String familyId, String adminUserId, Family.FamilySettings settings)
            throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.isAdmin(adminUserId)) {
            throw new RuntimeException("Only family admins can update settings");
        }

        family.setSettings(settings);
        familyRepository.updateFamily(family);
    }

    /**
     * Promote member to admin
     */
    public void promoteToAdmin(String familyId, String adminUserId, String memberUserId)
            throws ExecutionException, InterruptedException {
        Family family = familyRepository.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("Family not found");
        }

        if (!family.isAdmin(adminUserId)) {
            throw new RuntimeException("Only family admins can promote members");
        }

        if (!family.isMember(memberUserId)) {
            throw new RuntimeException("User is not a member of this family");
        }

        family.addAdmin(memberUserId);
        familyRepository.updateFamily(family);

        // Update user role
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("familyRole", "admin");
        userRepository.updateUserFields(memberUserId, userUpdates);
    }
}