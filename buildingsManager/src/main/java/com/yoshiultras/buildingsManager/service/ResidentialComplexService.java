package com.yoshiultras.buildingsManager.service;

import com.yoshiultras.buildingsManager.repository.ResidentialComplexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class ResidentialComplexService {
    private final ResidentialComplexRepository residentialComplexRepository;

    @Lazy
    @Autowired
    public ResidentialComplexService(ResidentialComplexRepository residentialComplexRepository) {
        this.residentialComplexRepository = residentialComplexRepository;
    }
}
