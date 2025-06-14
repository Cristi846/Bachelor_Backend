package com.backendBachelor.Mapper;

import com.backendBachelor.Dto.FamilyDto;
import com.backendBachelor.Dto.FamilyInvitationDto;
import com.backendBachelor.Entity.Family;
import com.backendBachelor.Entity.FamilyInvitation;

public class FamilyMapper {

    public static FamilyDto toDto(Family family) {
        if (family == null) {
            return null;
        }

        FamilyDto dto = new FamilyDto();
        dto.setId(family.getId());
        dto.setName(family.getName());
        dto.setCreatedBy(family.getCreatedBy());
        dto.setCreatedAt(family.getCreatedAt());
        dto.setMembers(family.getMembers());
        dto.setAdminUsers(family.getAdminUsers());
        dto.setSharedBudget(family.getSharedBudget());
        dto.setSettings(family.getSettings());

        return dto;
    }

    public static Family toEntity(FamilyDto dto) {
        if (dto == null) {
            return null;
        }

        Family family = new Family();
        family.setId(dto.getId());
        family.setName(dto.getName());
        family.setCreatedBy(dto.getCreatedBy());
        family.setCreatedAt(dto.getCreatedAt());
        family.setMembers(dto.getMembers());
        family.setAdminUsers(dto.getAdminUsers());
        family.setSharedBudget(dto.getSharedBudget());
        family.setSettings(dto.getSettings());

        return family;
    }

    public static FamilyInvitationDto toInvitationDto(FamilyInvitation invitation) {
        if (invitation == null) {
            return null;
        }

        FamilyInvitationDto dto = new FamilyInvitationDto();
        dto.setId(invitation.getId());
        dto.setFamilyId(invitation.getFamilyId());
        dto.setFamilyName(invitation.getFamilyName());
        dto.setInvitedBy(invitation.getInvitedBy());
        dto.setInvitedByName(invitation.getInvitedByName());
        dto.setInvitedEmail(invitation.getInvitedEmail());
        dto.setInvitedUserId(invitation.getInvitedUserId());
        dto.setStatus(invitation.getStatus());
        dto.setCreatedAt(invitation.getCreatedAt());
        dto.setExpiresAt(invitation.getExpiresAt());
        dto.setRespondedAt(invitation.getRespondedAt());

        return dto;
    }

    public static FamilyInvitation toInvitationEntity(FamilyInvitationDto dto) {
        if (dto == null) {
            return null;
        }

        FamilyInvitation invitation = new FamilyInvitation();
        invitation.setId(dto.getId());
        invitation.setFamilyId(dto.getFamilyId());
        invitation.setFamilyName(dto.getFamilyName());
        invitation.setInvitedBy(dto.getInvitedBy());
        invitation.setInvitedByName(dto.getInvitedByName());
        invitation.setInvitedEmail(dto.getInvitedEmail());
        invitation.setInvitedUserId(dto.getInvitedUserId());
        invitation.setStatus(dto.getStatus());
        invitation.setCreatedAt(dto.getCreatedAt());
        invitation.setExpiresAt(dto.getExpiresAt());
        invitation.setRespondedAt(dto.getRespondedAt());

        return invitation;
    }
}