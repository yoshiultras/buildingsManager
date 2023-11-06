package com.yoshiultras.buildingsManager.service;

import com.yoshiultras.buildingsManager.dao.HouseDao;
import com.yoshiultras.buildingsManager.dao.ResidentialComplexDao;
import com.yoshiultras.buildingsManager.model.Apartment;
import com.yoshiultras.buildingsManager.model.House;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.repository.HouseRepository;
import com.yoshiultras.buildingsManager.repository.ResidentialComplexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final ResidentialComplexRepository residentialComplexRepository;
    private final ApartmentService apartmentService;

    @Autowired
    public HouseService(ApartmentService apartmentService, HouseRepository houseRepository, ResidentialComplexRepository residentialComplexRepository) {
        this.apartmentService = apartmentService;
        this.houseRepository = houseRepository;
        this.residentialComplexRepository = residentialComplexRepository;
    }

    public List<House> getHouses() {
        return houseRepository.findAll();
    }
    public Optional<House> getHouseById(Long id) {
        return houseRepository.findById(id);
    }
    public List<HouseDao> getHousesDao() {
        return transfer(getHouses());
    }
    private List<HouseDao> transfer(List<House> list) {
        List<HouseDao> res = new ArrayList<>();
        for (House house : list) {
            List<Apartment> aps = apartmentService.getByHouseId(house.getId());
            HouseDao dao = new HouseDao(residentialComplexRepository.findNameById(house.getComplexId()),
                                        house.getStreet(),
                                        house.getHouseNumber(),
                                        house.getStatus(),
                                        apartmentService.getSoldAmount(aps),
                                        apartmentService.getReadyAmount(aps));
            res.add(dao);
        }
        return res;
    }

    public House getHouseByNumber(String number) {
        return houseRepository.findByNumber(number);
    }
    public List<ResidentialComplexDao> getAllComplex() {
        return houseRepository.findAllComplex();
    }
    public void update(House house) {
        houseRepository.update(house.getComplexId(), house.getStreet(), house.getHouseNumber(), house.getAddedValue(), house.getBuildingCost(), house.getId());
    }
    public void save(House house) {
        houseRepository.save(house);
    }

    public List<HouseDao> getHousesDaoByComplexId(Long id) {
        return transfer(getHousesByComplexId(id));
    }

    private List<House> getHousesByComplexId(Long id) {
        return houseRepository.findAllByComplexId(id);
    }

    public String findComplexNameById(Long houseId) {
        return houseRepository.findComplexNameById(houseId);
    }
    public String getComplexNameByHouseNumber(String number) {
        return houseRepository.findComplexNameByNumber(number);
    }
}
