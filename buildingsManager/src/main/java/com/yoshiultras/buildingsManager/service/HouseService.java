package com.yoshiultras.buildingsManager.service;

import com.yoshiultras.buildingsManager.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HouseService {

    private final HouseRepository houseRepository;

    @Lazy
    @Autowired
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }
}
