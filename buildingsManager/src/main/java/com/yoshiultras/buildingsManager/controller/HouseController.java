package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.dao.ResidentialComplexDao;
import com.yoshiultras.buildingsManager.model.House;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class HouseController implements FXMLController, Initializable {
    private House house;
    private ObservableList<ResidentialComplex> complexList;

    @Autowired
    private HouseService houseService;
    @Autowired
    private FXMLControllerUtils fxmlControllerUtils;
    @FXML
    private TextField street, houseNumber, addedValue, buildingCost;
    @FXML
    private ChoiceBox<ResidentialComplex> complex;
    @FXML
    private Button returnButton, editButton, addButton;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setDisable(true);
        addButton.setVisible(false);
    }

    public void setHouse(House house) {
        this.house = house;
    }
    public void setComplex(List<ResidentialComplex> list) {
        complexList = FXCollections.observableList(list);
        complex.setItems(complexList);
    }
    public void setText() {
        street.setText(house.getStreet());
        houseNumber.setText(house.getHouseNumber());
        addedValue.setText(house.getAddedValue().toString());
        buildingCost.setText(house.getBuildingCost().toString());
        for (ResidentialComplex residentialComplex : complexList) {
            if (Objects.equals(residentialComplex.getId(), house.getComplexId())) complex.setValue(residentialComplex);
        }
    }
    public void goBack(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "houseList.fxml");
    }
    public void edit(ActionEvent event) {
        try {
            errorLabel.setVisible(false);
            house.setStreet(street.getText());
            house.setHouseNumber(houseNumber.getText());
            house.setComplexId(complex.getValue().getId());
            house.setAddedValue(Integer.valueOf(addedValue.getText()));
            house.setBuildingCost(Integer.valueOf(buildingCost.getText()));
            houseService.update(house);
        } catch (Exception e) {
            errorLabel.setVisible(true);
        }
    }

    public void setAddMode() {
        editButton.setDisable(true);
        editButton.setVisible(false);
        addButton.setDisable(false);
        addButton.setVisible(true);
    }

    public void add(ActionEvent event) {
        try {
            errorLabel.setVisible(false);
            House newHouse = new House(street.getText(), houseNumber.getText(), 0, 0, complex.getValue().getId(), "", Integer.valueOf(addedValue.getText()), Integer.valueOf(buildingCost.getText()));
            houseService.save(newHouse);
        } catch (Exception e) {
            errorLabel.setVisible(true);
        }
    }
}