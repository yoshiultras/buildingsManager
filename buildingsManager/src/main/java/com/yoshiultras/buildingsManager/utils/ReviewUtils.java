package com.yoshiultras.buildingsManager.utils;

import com.yoshiultras.buildingsManager.dao.ApartmentDao;
import com.yoshiultras.buildingsManager.model.House;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.service.ApartmentService;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.service.ResidentialComplexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewUtils {

    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private ResidentialComplexService residentialComplexService;

    public Double getValue(List<ApartmentDao> list, Double squareCost, Double roomCost, Double addedCost) {
        Double res = 0.0;
        for (ApartmentDao apartmentDao : list) {
            res += getApartmentCost(apartmentDao, squareCost, roomCost, addedCost);
        }
        return res;
    }

    public Double getValueSold(List<ApartmentDao> list, Double squareCost, Double roomCost, Double addedCost) {
        Double res = 0.0;
        for (ApartmentDao apartmentDao : list) {
            if (apartmentDao.getStatusSale().equals("sold")) {
                res += getApartmentCost(apartmentDao, squareCost, roomCost, addedCost);
            }

        }
        return res;
    }

    public Double getValueUnsold(List<ApartmentDao> list, Double squareCost, Double roomCost, Double addedCost) {
        Double res = 0.0;
        for (ApartmentDao apartmentDao : list) {
            if (!apartmentDao.getStatusSale().equals("sold")) {
                res += getApartmentCost(apartmentDao, squareCost, roomCost, addedCost);
            }
        }
        return res;
    }

    public Double getCost(List<ApartmentDao> list, String complexName, String houseNumber) {
        Double res = 0.0;
        House house = houseService.getHouseByNumber(list.get(0).getAddress().split(", ")[1]);
        for (ApartmentDao apartmentDao : list) {
            res += apartmentDao.getAdditionalCostOfFinishing() / 1_000_000 + apartmentDao.getCostApartmentConstruction() / 1_000_000;
        }
        if (houseNumber != null) {
            res += house.getBuildingCost() / 1_000_000;
        }
        else if (complexName != null) {
            res += residentialComplexService.getById(house.getComplexId()).orElseThrow().getBuildingCost() / 1_000_000;
        }
        else {
            for (House houseServiceHouse : houseService.getHouses()) {
                res += houseServiceHouse.getBuildingCost() / 1_000_000;
            }
            for (ResidentialComplex residentialComplex : residentialComplexService.getAll()) {
                res += residentialComplex.getBuildingCost() / 1_000_000;
            }
        }
        return res;
    }
    private Double getApartmentCost(ApartmentDao apartmentDao, Double squareCost, Double roomCost, Double addedCost) {
        House house = houseService.getHouseByNumber(apartmentDao.getAddress().split(", ")[1]);
        return (apartmentDao.getSquare() * squareCost + apartmentDao.getRooms() * roomCost + addedCost +
                house.getAddedValue() + residentialComplexService.getById(house.getComplexId()).orElseThrow().getAddedValue()) / 1_000_000;
    }
}
