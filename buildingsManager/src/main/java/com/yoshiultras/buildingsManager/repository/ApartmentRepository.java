package com.yoshiultras.buildingsManager.repository;

import com.yoshiultras.buildingsManager.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
}
