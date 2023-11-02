package com.yoshiultras.buildingsManager.repository;

import com.yoshiultras.buildingsManager.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

}
