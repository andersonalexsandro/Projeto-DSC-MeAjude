package com.si.meAjude.service.dtos.donation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.si.meAjude.models.Donation;
import com.si.meAjude.repositories.CampaignRepository;
import com.si.meAjude.repositories.UserRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DonationSaveDTO(
        @NotNull
        Long userId,
        @NotNull
        Long campaignId,
        @NotNull
        @Min(value = 1)
        BigDecimal value) {

        public Donation toDonation(CampaignRepository campaignRepository, UserRepository donorRepository){
                Donation donation = new Donation();
                donation.setCampaign(campaignRepository.getById(campaignId));
                donation.setUser(donorRepository.getById(userId));
                donation.setDate(LocalDate.now());
                donation.setDonationValue(value);
                return donation;
        }
}
