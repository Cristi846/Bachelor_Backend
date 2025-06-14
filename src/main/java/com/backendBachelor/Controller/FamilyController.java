package com.backendBachelor.Controller;

import com.backendBachelor.Dto.*;
import com.backendBachelor.Entity.Family;
import com.backendBachelor.Entity.FamilyInvitation;
import com.backendBachelor.Service.FamilyService;
import com.backendBachelor.Mapper.FamilyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/family")
@CrossOrigin(origins = "*")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    /**
     * Create a new family
     */
    @PostMapping("/create")
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody CreateFamilyRequest request) {
        try {
            String familyId = familyService.createFamily(request.getUserId(), request.getFamilyName());
            Family family = familyService.getFamilyById(familyId);
            FamilyDto familyDto = FamilyMapper.toDto(family);

            CreateFamilyResponse response = new CreateFamilyResponse(
                    true,
                    "Family created successfully",
                    familyId,
                    familyDto
            );
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().body(
                    new CreateFamilyResponse(false, "Database error: " + e.getMessage(), null, null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new CreateFamilyResponse(false, e.getMessage(), null, null)
            );
        }
    }

    /**
     * Invite user to family
     */
    @PostMapping("/invite")
    public ResponseEntity<Map<String, Object>> inviteUser(@RequestBody InviteUserRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String invitationId = familyService.inviteUserToFamily(
                    request.getFamilyId(),
                    request.getInviterUserId(),
                    request.getInviteeEmail()
            );

            response.put("success", true);
            response.put("message", "Invitation sent successfully");
            response.put("invitationId", invitationId);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Accept family invitation
     */
    @PostMapping("/accept-invitation/{invitationId}")
    public ResponseEntity<Map<String, Object>> acceptInvitation(
            @PathVariable String invitationId,
            @RequestBody AcceptInvitationRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.acceptFamilyInvitation(invitationId, request.getUserId());

            response.put("success", true);
            response.put("message", "Invitation accepted successfully");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Decline family invitation
     */
    @PostMapping("/decline-invitation/{invitationId}")
    public ResponseEntity<Map<String, Object>> declineInvitation(
            @PathVariable String invitationId,
            @RequestBody AcceptInvitationRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.declineFamilyInvitation(invitationId, request.getUserId());

            response.put("success", true);
            response.put("message", "Invitation declined");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get family information
     */
    @GetMapping("/{familyId}")
    public ResponseEntity<FamilyDto> getFamily(@PathVariable String familyId) {
        try {
            Family family = familyService.getFamilyById(familyId);
            if (family == null) {
                return ResponseEntity.notFound().build();
            }

            FamilyDto familyDto = FamilyMapper.toDto(family);
            return ResponseEntity.ok(familyDto);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get family by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<FamilyDto> getFamilyByUser(@PathVariable String userId) {
        try {
            Family family = familyService.getFamilyByUserId(userId);
            if (family == null) {
                return ResponseEntity.notFound().build();
            }

            FamilyDto familyDto = FamilyMapper.toDto(family);
            return ResponseEntity.ok(familyDto);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get family expenses
     */
    @GetMapping("/{familyId}/expenses")
    public ResponseEntity<List<ExpenseDto>> getFamilyExpenses(@PathVariable String familyId) {
        try {
            List<ExpenseDto> expenses = familyService.getFamilyExpenses(familyId);
            return ResponseEntity.ok(expenses);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update family shared budget
     */
    @PutMapping("/{familyId}/budget")
    public ResponseEntity<Map<String, Object>> updateSharedBudget(
            @PathVariable String familyId,
            @RequestBody UpdateFamilyBudgetRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.updateSharedBudget(familyId, request.getUserId(), request.getSharedBudget());

            response.put("success", true);
            response.put("message", "Family budget updated successfully");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Remove family member
     */
    @DeleteMapping("/{familyId}/members")
    public ResponseEntity<Map<String, Object>> removeMember(
            @PathVariable String familyId,
            @RequestBody RemoveFamilyMemberRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.removeFamilyMember(familyId, request.getAdminUserId(), request.getMemberUserId());

            response.put("success", true);
            response.put("message", "Member removed successfully");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get family analytics
     */
    @GetMapping("/{familyId}/analytics")
    public ResponseEntity<FamilyAnalyticsResponse> getFamilyAnalytics(@PathVariable String familyId) {
        try {
            Map<String, Double> categorySpending = familyService.getFamilySpendingAnalysis(familyId);
            Map<String, Double> memberSpending = familyService.getMemberSpendingAnalysis(familyId);

            Family family = familyService.getFamilyById(familyId);
            double totalBudget = family != null ? family.getSharedBudget().getMonthlyBudget() : 0.0;
            double totalSpending = categorySpending.values().stream().mapToDouble(Double::doubleValue).sum();
            double remainingBudget = totalBudget - totalSpending;

            // Find over budget categories
            List<String> overBudgetCategories = categorySpending.entrySet().stream()
                    .filter(entry -> {
                        if (family != null && family.getSharedBudget().getCategoryBudgets().containsKey(entry.getKey())) {
                            double categoryBudget = family.getSharedBudget().getCategoryBudgets().get(entry.getKey());
                            return entry.getValue() > categoryBudget && categoryBudget > 0;
                        }
                        return false;
                    })
                    .map(Map.Entry::getKey)
                    .collect(java.util.stream.Collectors.toList());

            FamilyAnalyticsResponse response = new FamilyAnalyticsResponse();
            response.setCategorySpending(categorySpending);
            response.setMemberSpending(memberSpending);
            response.setTotalFamilySpending(totalSpending);
            response.setRemainingBudget(remainingBudget);
            response.setOverBudgetCategories(overBudgetCategories);

            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get pending invitations for user
     */
    @GetMapping("/invitations/pending/{email}")
    public ResponseEntity<List<FamilyInvitationDto>> getPendingInvitations(@PathVariable String email) {
        try {
            List<FamilyInvitation> invitations = familyService.getPendingInvitationsForUser(email);
            List<FamilyInvitationDto> invitationDtos = invitations.stream()
                    .map(FamilyMapper::toInvitationDto)
                    .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(invitationDtos);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get family invitations (for admins)
     */
    @GetMapping("/{familyId}/invitations")
    public ResponseEntity<List<FamilyInvitationDto>> getFamilyInvitations(
            @PathVariable String familyId,
            @RequestParam String adminUserId) {
        try {
            List<FamilyInvitation> invitations = familyService.getFamilyInvitations(familyId, adminUserId);
            List<FamilyInvitationDto> invitationDtos = invitations.stream()
                    .map(FamilyMapper::toInvitationDto)
                    .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(invitationDtos);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancel invitation
     */
    @DeleteMapping("/invitations/{invitationId}")
    public ResponseEntity<Map<String, Object>> cancelInvitation(
            @PathVariable String invitationId,
            @RequestParam String adminUserId) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.cancelFamilyInvitation(invitationId, adminUserId);

            response.put("success", true);
            response.put("message", "Invitation cancelled");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Leave family
     */
    @PostMapping("/{familyId}/leave")
    public ResponseEntity<Map<String, Object>> leaveFamily(
            @PathVariable String familyId,
            @RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.leaveFamily(familyId, userId);

            response.put("success", true);
            response.put("message", "Left family successfully");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete family
     */
    @DeleteMapping("/{familyId}")
    public ResponseEntity<Map<String, Object>> deleteFamily(
            @PathVariable String familyId,
            @RequestParam String creatorUserId) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.deleteFamily(familyId, creatorUserId);

            response.put("success", true);
            response.put("message", "Family deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Promote member to admin
     */
    @PostMapping("/{familyId}/promote")
    public ResponseEntity<Map<String, Object>> promoteToAdmin(
            @PathVariable String familyId,
            @RequestParam String adminUserId,
            @RequestParam String memberUserId) {
        Map<String, Object> response = new HashMap<>();
        try {
            familyService.promoteToAdmin(familyId, adminUserId, memberUserId);

            response.put("success", true);
            response.put("message", "Member promoted to admin successfully");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}