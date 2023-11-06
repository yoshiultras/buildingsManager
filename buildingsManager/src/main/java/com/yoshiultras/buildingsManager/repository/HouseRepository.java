package com.yoshiultras.buildingsManager.repository;

import com.yoshiultras.buildingsManager.dao.ResidentialComplexDao;
import com.yoshiultras.buildingsManager.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mapping.model.SpELExpressionEvaluator;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    @Query("SELECT h FROM House h JOIN ResidentialComplex c ON c.id = h.complexId ORDER BY c.name, h.status, h.houseNumber")
    List<House> findAll();

    @Query("SELECT h FROM House h WHERE h.houseNumber = ?1")
    House findByNumber(String number);

    @Query("SELECT DISTINCT new com.yoshiultras.buildingsManager.dao.ResidentialComplexDao(c.name, h.status, " +
            "count(h), c.city) FROM House h JOIN ResidentialComplex c ON c.id = h.complexId GROUP BY 1, 2, 4")
    List<ResidentialComplexDao> findAllComplex();

    @Modifying
    @Transactional
    @Query("UPDATE House h " +
            "SET h.complexId = :complexId, " +
            "h.street = :street, " +
            "h.houseNumber = :number, " +
            "h.addedValue = :addedValue, " +
            "h.buildingCost = :buildingCost " +
            "WHERE h.id = :id")
    void update(@Param("complexId") Long complexId,
                @Param("street") String street,
                @Param("number") String number,
                @Param("addedValue") Integer addedValue,
                @Param("buildingCost") Integer buildingCost,
                @Param("id") Long id);

    @Query("SELECT h FROM House h WHERE h.complexId = :id")
    List<House> findAllByComplexId(@Param("id") Long id);

    @Query("SELECT c.name FROM House h JOIN ResidentialComplex c ON c.id = h.complexId WHERE h.id = :id")
    String findComplexNameById(@Param("id") Long id);

    @Query("SELECT c.name FROM House h JOIN ResidentialComplex c ON c.id = h.complexId WHERE h.houseNumber = :number")
    String findComplexNameByNumber(@Param("number") String number);
}
