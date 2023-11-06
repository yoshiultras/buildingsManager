package com.yoshiultras.buildingsManager.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResidentialComplexDao {
    private String name;
    private String status;
    private Long housesNumber;
    private String city;

    @Override
    public String toString() {
        return name;
    }
}
