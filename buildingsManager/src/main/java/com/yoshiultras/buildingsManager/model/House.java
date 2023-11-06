package com.yoshiultras.buildingsManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "houses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class House {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name="id")
    private Long id;
    private String street;
    private String houseNumber;
    private Integer constructionCost;
    private Integer additionalCostApartment;
    private Long complexId;

    private String status;
    private Integer addedValue;
    private Integer buildingCost;

    public House(String street, String houseNumber, Integer constructionCost, Integer additionalCostApartment, Long complexId, String status, Integer addedValue, Integer buildingCost) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.constructionCost = constructionCost;
        this.additionalCostApartment = additionalCostApartment;
        this.complexId = complexId;

        this.status = status;
        this.addedValue = addedValue;
        this.buildingCost = buildingCost;
    }
}
