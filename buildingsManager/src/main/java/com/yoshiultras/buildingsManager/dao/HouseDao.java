package com.yoshiultras.buildingsManager.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class HouseDao {
    private String complex;
    private String street;
    private String number;
    private String status;
    private Integer apartmentsSold;
    private Integer apartmentsReady;
}
