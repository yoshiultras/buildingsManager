package com.yoshiultras.buildingsManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name="id")
    private Long id;
    private Long houseId;
    private Integer apartmentNumber;
    private Double square;
    private Integer rooms;
    private Integer entrance;
    private Integer storey;
    private String statusSale;
    private Integer additionalCostOfFinishing;
    private Integer costApartmentConstruction;

    public Apartment(Long houseId, Integer apartmentNumber, Double square, Integer rooms, Integer entrance, Integer storey, String statusSale, Integer additionalCostOfFinishing, Integer costApartmentConstruction) {
        this.houseId = houseId;
        this.apartmentNumber = apartmentNumber;
        this.square = square;
        this.rooms = rooms;
        this.entrance = entrance;
        this.storey = storey;
        this.statusSale = statusSale;
        this.additionalCostOfFinishing = additionalCostOfFinishing;
        this.costApartmentConstruction = costApartmentConstruction;
    }
}
