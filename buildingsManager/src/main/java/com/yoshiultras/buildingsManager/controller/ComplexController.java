package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.dao.HouseDao;
import com.yoshiultras.buildingsManager.dao.ResidentialComplexDao;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.service.ResidentialComplexService;
import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ComplexController implements FXMLController, Initializable {
    private ResidentialComplex residentialComplex;
    private HouseDao selectedHouse;
    @Autowired
    FXMLControllerUtils fxmlControllerUtils;
    @Autowired
    ResidentialComplexService residentialComplexService;
    @Autowired
    HouseService houseService;
    @FXML
    private TextField name, buildingCost, addedValue, city;
    @FXML
    private ChoiceBox<String> status;
    @FXML
    private Button addButton, editButton;
    @FXML
    private TableColumn<HouseDao, String> street, number;
    @FXML
    private TableView<HouseDao> table;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setDisable(true);
        addButton.setVisible(false);
    }
    public void setComplex(ResidentialComplex residentialComplex) {
        this.residentialComplex = residentialComplex;
    }
    public void setText() {
        status.setItems(FXCollections.observableArrayList("plan", "built", "building"));
        status.setValue(residentialComplex.getStatus());
        name.setText(residentialComplex.getName());
        buildingCost.setText(residentialComplex.getAddedValue().toString());
        addedValue.setText(residentialComplex.getBuildingCost().toString());
        city.setText(residentialComplex.getCity());

        ObservableList<HouseDao> houseList = FXCollections.observableList(houseService.getHousesDaoByComplexId(residentialComplex.getId()));
        street.setCellValueFactory(new PropertyValueFactory<HouseDao, String>("street"));
        number.setCellValueFactory(new PropertyValueFactory<HouseDao, String>("number"));
        TableView.TableViewSelectionModel<HouseDao> selectionModel = table.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if(newVal != null) {
                selectedHouse = newVal;
            }
        });
        table.setItems(houseList);
    }

    public void goBack(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "complexList.fxml");
    }
    public void edit(ActionEvent event) {
        try {
            errorLabel.setVisible(false);
            residentialComplex.setName(name.getText());
            residentialComplex.setStatus(status.getValue());
            residentialComplex.setBuildingCost(Integer.valueOf(buildingCost.getText()));
            residentialComplex.setAddedValue(Integer.valueOf(addedValue.getText()));
            residentialComplex.setCity(city.getText());
            if(residentialComplexService.validToSetPlan(residentialComplex) ||
                    !status.getValue().equals("plan")) residentialComplexService.update(residentialComplex);
        } catch (Exception e) {
            errorLabel.setVisible(true);
        }
    }
    public void add(ActionEvent event) {
        try {
            errorLabel.setVisible(false);
            ResidentialComplex newComplex = new ResidentialComplex(name.getText(), status.getValue(), city.getText(),
                    Integer.valueOf(buildingCost.getText()), Integer.valueOf(addedValue.getText()));
            residentialComplexService.save(newComplex);
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
}
