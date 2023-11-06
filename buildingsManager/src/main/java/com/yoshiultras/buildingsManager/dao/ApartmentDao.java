package com.yoshiultras.buildingsManager.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentDao {
    private Long id;
    private String complex;
    private String address;
    private Integer apartmentNumber;
    private Double square;
    private Integer rooms;
    private Integer entrance;
    private Integer storey;
    private String statusSale;
    private Integer additionalCostOfFinishing;
    private Integer costApartmentConstruction;
}
