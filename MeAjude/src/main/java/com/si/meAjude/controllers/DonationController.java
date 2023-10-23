package com.si.meAjude.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.si.meAjude.models.User;
import com.si.meAjude.models.enums.UserRole;
import com.si.meAjude.service.UserSerivce;
import com.si.meAjude.service.searchers.dtos.DonationSearchContent;
import com.si.meAjude.service.searchers.enums.DonationSearchCriterion;
import com.si.meAjude.service.DonationService;
import com.si.meAjude.service.dtos.donation.DonationDTO;
import com.si.meAjude.service.dtos.donation.DonationSaveDTO;
import com.si.meAjude.util.PageableUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    DonationService donationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DonationDTO saveDonation(@RequestBody @Valid DonationSaveDTO dto) {
        return donationService.save(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> getById(@PathVariable Long id,  Authentication authentication) {
        User requestUser = (User) authentication.getPrincipal();
        if(!requestUser.getRole().equals(UserRole.ADMIN)) if(!requestUser.getId().equals(id)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(donationService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<DonationDTO>> getAll(
            @PageableDefault(size = 10) Pageable page,
            @RequestParam(name = "sortField", required = false, defaultValue = "date") String sortField,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "campaignId", required = false) Long campaignId,
            @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) @RequestParam(name = "date", required = false)LocalDate date,
            Authentication authentication){

        page = PageableUtil.getPageableWithSort(page, sortField, sortDirection);
        User requestUser = (User) authentication.getPrincipal();
        DonationSearchContent searchContent = new DonationSearchContent(userId, campaignId, date, null);
        if (requestUser.getRole() != UserRole.ADMIN){
            if(userId != null && !userId.equals(requestUser.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            searchContent.setUserId(requestUser.getId());
        }
        return new ResponseEntity<>(donationService.getAll(page, searchContent), HttpStatus.OK);
    }
}
