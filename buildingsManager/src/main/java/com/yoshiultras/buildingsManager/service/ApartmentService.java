package com.yoshiultras.buildingsManager.service;

import com.yoshiultras.buildingsManager.dao.ApartmentDao;
import com.yoshiultras.buildingsManager.model.Apartment;
import com.yoshiultras.buildingsManager.model.House;
import com.yoshiultras.buildingsManager.repository.ApartmentRepository;
import com.yoshiultras.buildingsManager.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final HouseRepository houseRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository, HouseRepository houseRepository) {
        this.apartmentRepository = apartmentRepository;
        this.houseRepository = houseRepository;
    }

    public List<Apartment> getByHouseId(Long id) {
        return apartmentRepository.findAllByHouseId(id);
    }
    public Integer getSoldAmount(List<Apartment> list) {
        Integer res = 0;
        for (Apartment apartment : list) {
            if (apartment.getStatusSale().equals("sold")) res++;
        }
        return res;
    }
    public Integer getReadyAmount(List<Apartment> list) {
        Integer res = 0;
        for (Apartment apartment : list) {
            if (apartment.getStatusSale().equals("ready")) res++;
        }
        return res;
    }

    public List<Apartment> getAll() {
        return apartmentRepository.findAll();
    }
    private List<ApartmentDao> transfer(List<Apartment> list) {
        List<ApartmentDao> res = new ArrayList<>();
        for (Apartment apartment : list) {
            House house = houseRepository.findById(apartment.getHouseId()).orElseThrow();
            ApartmentDao apartmentDao = new ApartmentDao(apartment.getId(), houseRepository.findComplexNameById(apartment.getHouseId()),
                    "ул. "+house.getStreet()+", "+house.getHouseNumber(),apartment.getApartmentNumber(), apartment.getSquare(), apartment.getRooms(),
                    apartment.getEntrance(), apartment.getStorey(),  apartment.getStatusSale(), apartment.getAdditionalCostOfFinishing(), apartment.getCostApartmentConstruction());
            res.add(apartmentDao);
        }
        return res;
    }

    public List<ApartmentDao> getAllDao() {
        return transfer(getAll());
    }

    public Optional<Apartment> getById(Long id) {
        return apartmentRepository.findById(id);
    }

    public boolean isValidToSetSold(Apartment apartment) {
        return apartmentRepository.findPlanedComplex(apartment.getId()).isEmpty();
    }

    public void update(Apartment apartment) {
        apartmentRepository.update(apartment.getId(), apartment.getHouseId(), apartment.getApartmentNumber(),
                apartment.getEntrance(), apartment.getStorey(), apartment.getSquare(), apartment.getRooms(),
                apartment.getStatusSale(), apartment.getCostApartmentConstruction(), apartment.getAdditionalCostOfFinishing());
    }

    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }
}
