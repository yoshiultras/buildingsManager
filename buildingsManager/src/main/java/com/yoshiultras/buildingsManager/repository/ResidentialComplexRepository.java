package com.yoshiultras.buildingsManager.repository;

import com.yoshiultras.buildingsManager.model.House;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ResidentialComplexRepository extends JpaRepository<ResidentialComplex, Long> {

    @Query("SELECT DISTINCT c.city FROM ResidentialComplex c")
    List<String> findAllCity();

    @Query("SELECT c.name FROM ResidentialComplex c WHERE c.id = :id")
    String findNameById(@Param("id")Long id);

    @Query("SELECT c FROM ResidentialComplex c WHERE c.name = :name")
    ResidentialComplex findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("update ResidentialComplex c " +
            "SET c.name = :name, " +
            "c.status = :status, " +
            "c.buildingCost = :buildingCost, " +
            "c.addedValue = :addedValue, " +
            "c.city = :city " +
            "WHERE c.id = :id")
    void update(@Param("name") String name,
                @Param("status") String status,
                @Param("buildingCost") Integer buildingCost,
                @Param("addedValue") Integer addedValue,
                @Param("city") String city,
                @Param("id") Long id);

    @Query("SELECT DISTINCT c FROM ResidentialComplex c JOIN House h ON h.complexId = c.id JOIN Apartment a ON a.houseId = h.id WHERE c.id = :id AND a.statusSale = 'sold'")
    Optional<ResidentialComplex> findBoughtApartment(@Param("id") Long id);

}
