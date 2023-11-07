package com.yoshiultras.buildingsManager.service;


import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.repository.ResidentialComplexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentialComplexService {
    @Autowired
    private ResidentialComplexRepository residentialComplexRepository;

    public List<ResidentialComplex> getAll() {
        return residentialComplexRepository.findAll();
    }
    public Optional<ResidentialComplex> getById(Long id) {
        return residentialComplexRepository.findById(id);
    }
    public ResidentialComplex getByName(String name) {
        return residentialComplexRepository.findByName(name);
    }
    public List<String> getAllCity() {
        return residentialComplexRepository.findAllCity();
    }

    public void update(ResidentialComplex residentialComplex) {
        residentialComplexRepository.update(residentialComplex.getName(), residentialComplex.getStatus(),
                residentialComplex.getBuildingCost(), residentialComplex.getAddedValue(), residentialComplex.getCity(), residentialComplex.getId());
    }
    public void save(ResidentialComplex residentialComplex) {
        residentialComplexRepository.save(residentialComplex);
    }
    public boolean validToSetPlan(ResidentialComplex residentialComplex) {
        return residentialComplexRepository.findBoughtApartment(residentialComplex.getId()).isEmpty();
    }

    public String getNameByHouseId(Long id) {
        return residentialComplexRepository.findNameByHouseId(id);
    }
}
