package com.yoshiultras.buildingsManager.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class ApartmentUtilsTests {

    @Autowired
    ApartmentUtils apartmentUtils;

    @Test
    void levenshtein1() {
        assert apartmentUtils.isValid("строка", "собака");
    }
    @Test
    void levenshtein2() {
        assert apartmentUtils.isValid("уборка", "затворка");
    }
}
