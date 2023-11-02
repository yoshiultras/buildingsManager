package com.yoshiultras.buildingsManager.service;

import com.yoshiultras.buildingsManager.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;

    @Lazy
    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }
}
