package com.yoshiultras.buildingsManager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class House {
    @Id
    private Long id;
    private String street;
    private String houseNumber;
    private Integer constructionCost;
    private Integer additionalCost;
    private String housingCompanyName;
    private String city;
    private String status;
    private Integer addedValue;
    private Integer buildingCost;
}
