package com.si.meAjude.service.impl;


import com.si.meAjude.exceptions.*;
import com.si.meAjude.models.Campaign;
import com.si.meAjude.models.User;
import com.si.meAjude.repositories.CampaignRepository;
import com.si.meAjude.repositories.DonationRepository;
import com.si.meAjude.repositories.UserRepository;
import com.si.meAjude.service.CampaignService;
import com.si.meAjude.service.dtos.campaign.CampaignDTO;
import com.si.meAjude.service.dtos.campaign.CampaignSaveDTO;
import com.si.meAjude.service.dtos.campaign.CampaignUpdateDTO;
import com.si.meAjude.service.searchers.campaign.CampaignSearchContent;
import com.si.meAjude.service.searchers.campaign.CampaignSearcher;
import com.si.meAjude.service.searchers.campaign.CampaignSearcherFactoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    CampaignSearcherFactoryService campaignSearcherFactoryService;

    public CampaignDTO save(CampaignSaveDTO campaignDTO){
        Campaign campaign = new Campaign();
        User creator = getUserInDateBase(campaignDTO.creatorId());
        campaign.setCreator(creator);
        campaign.setTitle(campaignDTO.title());
        campaign.setDescription(campaignDTO.description());
        campaign.setGoal(campaignDTO.goal());
        campaign.setStartingDate(LocalDate.now());
        campaign.setFinalDate(campaignDTO.finalDate());
        campaign.setActive(true);
        campaign.setDeleted(false);
        campaign.setRaisedMoney(BigDecimal.ZERO);
        return new CampaignDTO(campaignRepository.save(campaign));
    }

    private User getUserInDateBase(Long id){
        User user = userRepository.getById(id);
        if(user.isDeleted()) throw new EntityNotFoundException("Unable to find User with id "+ id);
        return user;
    }

    @Override
    public CampaignDTO getCampaign(Long id) {
        return new CampaignDTO(getCampaignInDateBase(id));
    }

    @Override
    public Page<CampaignDTO> getAll(Pageable page, CampaignSearchContent searchContent){
        CampaignSearcher searcher = campaignSearcherFactoryService.getSearcher(searchContent.criterion());
        return searcher.search(page, searchContent).map(CampaignDTO::new);
    }

    @Override
    public CampaignDTO update(CampaignUpdateDTO updateDTO) throws InvalidDateException, InvalidGoalException, InvalidDescriptionException, InvalidTitleException, InvalidCreatorException {

        Campaign c = getCampaignInDateBase(updateDTO.id());

        if(updateDTO.active() != null && updateDTO.active() != c.isActive()) changeState(updateDTO.active(), updateDTO.id());
        if(updateDTO.finalDate() != null && !updateDTO.finalDate().equals(c.getFinalDate())) changeFinalDate(updateDTO.finalDate(), updateDTO.id());
        if(updateDTO.goal() != null && !updateDTO.goal().equals(c.getGoal())) changeGoal(updateDTO.goal(), updateDTO.id());
        if(updateDTO.description() != null && !updateDTO.description().equals(c.getDescription())) changeDescription(updateDTO.description(), updateDTO.id());
        if(updateDTO.title() != null && !updateDTO.title().equals(c.getTitle())) changeTitle(updateDTO.title(), updateDTO.id());
        return new CampaignDTO(c);
    }

    @Override
    public CampaignDTO logicRemoveCampaign(long id) {
        Campaign c = getCampaignInDateBase(id);
        c.setDeleted(true);
        campaignRepository.save(c);
        return new CampaignDTO(c);
    }

    private Campaign getCampaignInDateBase(Long id){
        Campaign campaign = campaignRepository.getById(id);
        if(campaign.isDeleted()) throw new EntityNotFoundException("Unable to find Campaign with id "+ id);
        return campaign;
    }

    private CampaignDTO changeState(boolean state, long id) {
        Campaign c = getCampaignInDateBase(id);
        c.setActive(state);
        campaignRepository.save(c);
        return new CampaignDTO(c);
    }


    private CampaignDTO changeTitle(String title, long id) throws InvalidTitleException {
        if(title.length() > 100 || title.trim().isEmpty())
            throw new InvalidTitleException("Invalid Tittle!");
        Campaign c = getCampaignInDateBase(id);
        c.setTitle(title);
        campaignRepository.save(c);
        return new CampaignDTO(c);
    }

    private CampaignDTO changeDescription(String description, long id) throws InvalidDescriptionException {
        if(description.length() > 1000)
            throw new InvalidDescriptionException("Invalid discription !");
        Campaign c = getCampaignInDateBase(id);
        c.setDescription(description);
        campaignRepository.save(c);
        return new CampaignDTO(c);
    }

    private CampaignDTO changeGoal(BigDecimal goal, long id) throws InvalidGoalException {
        if(goal.doubleValue() <= 0)
            throw new InvalidGoalException("Invalid goal!");
        Campaign c = getCampaignInDateBase(id);
        c.setGoal(goal);
        campaignRepository.save(c);
        return new CampaignDTO(c);
    }

    private CampaignDTO changeFinalDate(LocalDate finalDate, long id) throws InvalidDateException {
        Campaign c = getCampaignInDateBase(id);
        if(finalDate.isBefore(c.getStartingDate()) || finalDate.isEqual(c.getStartingDate()))
            throw new InvalidDateException("Invalid date!");
        c.setFinalDate(finalDate);
        campaignRepository.save(c);
        return new CampaignDTO(c);
    }
}
