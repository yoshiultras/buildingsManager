package com.yoshiultras.buildingsManager.repository;

import com.yoshiultras.buildingsManager.model.Apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query("SELECT a FROM Apartment a WHERE a.houseId = ?1")
    List<Apartment> findAllByHouseId(Long id);
}
