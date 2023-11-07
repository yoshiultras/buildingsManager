package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.dao.ApartmentDao;
import com.yoshiultras.buildingsManager.model.Apartment;
import com.yoshiultras.buildingsManager.model.House;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.service.ApartmentService;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.service.ResidentialComplexService;
import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ApartmentController implements FXMLController, Initializable {
    @Autowired
    FXMLControllerUtils fxmlControllerUtils;
    @Autowired
    ResidentialComplexService residentialComplexService;
    @Autowired
    ApartmentService apartmentService;
    @Autowired
    HouseService houseService;
    private Apartment apartment;
    private FilteredList<String> filteredHouses;
    @FXML
    private TextField apartmentNumber, square, storey, entrance, addedValue, finishingCost, rooms;
    @FXML
    private ChoiceBox<String> status, complex, house;
    @FXML
    private Button addButton, editButton;
    @FXML
    private Label errorLabel;
    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public void setText() {
        apartmentNumber.setText(String.valueOf(apartment.getApartmentNumber()));
        square.setText(String.valueOf(apartment.getSquare()));
        storey.setText(String.valueOf(apartment.getStorey()));
        entrance.setText(String.valueOf(apartment.getEntrance()));
        addedValue.setText(String.valueOf(apartment.getCostApartmentConstruction()));
        finishingCost.setText(String.valueOf(apartment.getAdditionalCostOfFinishing()));
        rooms.setText(String.valueOf(apartment.getRooms()));
        status.setValue(apartment.getStatusSale());
        complex.setValue(residentialComplexService.getNameByHouseId(apartment.getHouseId()));
        house.setValue(houseService.getHouseById(apartment.getHouseId()).orElseThrow().getHouseNumber());
        addButton.setVisible(false);
        addButton.setDisable(true);
        editButton.setVisible(true);
        editButton.setDisable(false);
    }
    public void goBack(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "apartmentList.fxml");
    }
    public void edit(ActionEvent event) {
        try {
            errorLabel.setVisible(false);
            apartment.setApartmentNumber(Integer.valueOf(apartmentNumber.getText()));
            apartment.setSquare(Double.valueOf(square.getText()));
            apartment.setStorey(Integer.valueOf(storey.getText()));
            apartment.setEntrance(Integer.valueOf(entrance.getText()));
            apartment.setCostApartmentConstruction(Integer.valueOf(addedValue.getText()));
            apartment.setAdditionalCostOfFinishing(Integer.valueOf(finishingCost.getText()));
            apartment.setRooms(Integer.valueOf(rooms.getText()));
            apartment.setStatusSale(status.getValue());
            apartment.setHouseId(houseService.getHouseByNumber(house.getValue()).getId());
            if(!status.getValue().equals("sold") || apartmentService.isValidToSetSold(apartment)) apartmentService.update(apartment);
        } catch (Exception e) {
            errorLabel.setVisible(true);
        }

    }
    public void add(ActionEvent event) {
        try {
            errorLabel.setVisible(false);
            Apartment newApartment = new Apartment(houseService.getHouseByNumber(house.getValue()).getId(),
                    Integer.parseInt(apartmentNumber.getText()), Double.parseDouble(square.getText()), Integer.parseInt(rooms.getText()),
                    Integer.parseInt(entrance.getText()), Integer.parseInt(storey.getText()), status.getValue(),
                    Integer.parseInt(finishingCost.getText()), Integer.parseInt(addedValue.getText()));
            apartmentService.save(newApartment);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editButton.setVisible(false);
        editButton.setDisable(true);
        List<ResidentialComplex> complexes = residentialComplexService.getAll();
        List<House> houses = houseService.getHouses();
        List<String> complexStrings = new ArrayList<>();
        List<String> houseStrings = new ArrayList<>();
        for (ResidentialComplex residentialComplex : complexes) {
            complexStrings.add(residentialComplex.getName());
        }
        for (House h : houses) {
            houseStrings.add(h.getHouseNumber());
        }
        complex.setItems(FXCollections.observableList(complexStrings));
        filteredHouses = new FilteredList<>(FXCollections.observableList(houseStrings));
        house.setItems(filteredHouses);
        status.setItems(FXCollections.observableArrayList("sold", "ready"));

        complex.valueProperty().addListener((observable, oldValue, newValue) -> houseFilter());
    }

    public void houseFilter() {
        house.setValue(null);
        filteredHouses.setPredicate(
                t -> houseService.getComplexNameByHouseNumber(t).equals(complex.getValue()) || complex.getValue() == null
        );
    }
}
