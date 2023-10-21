package com.si.meAjude.models.comparators;

import com.si.meAjude.service.dtos.campanha.CampanhaDTO;
import com.si.meAjude.service.dtos.campanha.CampanhaGetDTO;

import java.util.Comparator;

public class DataComparator implements Comparator<CampanhaGetDTO> {
    @Override
    public int compare(CampanhaGetDTO o1, CampanhaGetDTO o2) {
        return o1.dataInicio().compareTo(o2.dataInicio());
    }
}
