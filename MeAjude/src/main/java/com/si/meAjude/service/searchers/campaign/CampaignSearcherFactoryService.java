package com.si.meAjude.service.searchers.campaign;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CampaignSearcherFactoryService {

    private Map<CampaignSearchCriterion, CampaignSearcher> searchers = new HashMap<>();

    public CampaignSearcherFactoryService(Set<CampaignSearcher> searchers){
        searchers.forEach(searcher -> this.searchers.put(searcher.getCriterion(), searcher));
    }

    public CampaignSearcher getSearcher(CampaignSearchCriterion criterion){
        return searchers.get(criterion);
    }

}
