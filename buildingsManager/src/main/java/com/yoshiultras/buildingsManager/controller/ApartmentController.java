package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.model.Apartment;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApartmentController implements FXMLController{
    @Autowired
    FXMLControllerUtils fxmlControllerUtils;
    public void setApartment(Apartment orElseThrow) {
    }

    public void setText() {
    }
    public void goBack(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "apartmentList.fxml");
    }
    public void edit(ActionEvent event) {
//        residentialComplex.setName(name.getText());
//        residentialComplex.setStatus(status.getValue());
//        residentialComplex.setBuildingCost(Integer.valueOf(buildingCost.getText()));
//        residentialComplex.setAddedValue(Integer.valueOf(addedValue.getText()));
//        residentialComplex.setCity(city.getText());
//        if(residentialComplexService.validToSetPlan(residentialComplex) ||
//                !status.getValue().equals("plan")) residentialComplexService.update(residentialComplex);

    }
    public void add(ActionEvent event) {
//        ResidentialComplex newComplex = new ResidentialComplex(name.getText(), status.getValue(), city.getText(),
//                Integer.valueOf(buildingCost.getText()), Integer.valueOf(addedValue.getText()));
//        residentialComplexService.save(newComplex);
    }
}
