package com.yoshiultras.buildingsManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "complexes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResidentialComplex {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name="id")
    private Long id;
    private String name;
    private String status;
    private String city;
    private Integer addedValue;
    private Integer buildingCost;

    public ResidentialComplex(String name, String status, String city, Integer addedValue, Integer buildingCost) {
        this.name = name;
        this.status = status;
        this.city = city;
        this.addedValue = addedValue;
        this.buildingCost = buildingCost;
    }

    @Override
    public String toString() {
        return name;
    }
}
