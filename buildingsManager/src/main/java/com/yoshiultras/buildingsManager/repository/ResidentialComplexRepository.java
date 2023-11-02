package com.yoshiultras.buildingsManager.repository;

import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentialComplexRepository extends JpaRepository<ResidentialComplex, Long> {
}
