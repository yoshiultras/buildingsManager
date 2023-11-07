package com.yoshiultras.buildingsManager.repository;

import com.yoshiultras.buildingsManager.model.Apartment;

import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query("SELECT a FROM Apartment a WHERE a.houseId = ?1")
    List<Apartment> findAllByHouseId(Long id);

    @Query("SELECT c FROM ResidentialComplex c JOIN House h ON h.complexId = c.id JOIN Apartment a ON a.houseId = h.id WHERE a.id = ?1 AND c.status = 'plan'")
    Optional<ResidentialComplex> findPlanedComplex(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Apartment a " +
            "SET a.houseId = ?2, a.apartmentNumber = ?3, a.entrance = ?4, " +
            "a.storey = ?5, a.square = ?6, a.rooms = ?7, a.statusSale = ?8, a.costApartmentConstruction = ?9, a.additionalCostOfFinishing = ?10 " +
            "WHERE a.id = ?1")
    void update(Long id, Long houseId, Integer apartmentNumber, Integer entrance, Integer storey, Double square, Integer rooms, String statusSale, Integer costApartmentConstruction, Integer additionalCostOfFinishing);

}
