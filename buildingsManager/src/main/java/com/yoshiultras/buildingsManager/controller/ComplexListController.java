package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.dao.HouseDao;
import com.yoshiultras.buildingsManager.dao.ResidentialComplexDao;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.service.ResidentialComplexService;
import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ComplexListController implements FXMLController, Initializable {
    private ResidentialComplexDao selectedComplex;
    private ObservableList<ResidentialComplexDao> complexList;
    private FilteredList<ResidentialComplexDao> filteredList;

    @Autowired
    private FXMLControllerUtils fxmlControllerUtils;
    @Autowired
    private HouseService houseService;
    @Autowired
    private ResidentialComplexService residentialComplexService;
    @FXML
    private TableView<ResidentialComplexDao> table;
    @FXML
    private TableColumn<ResidentialComplexDao, String> nameCol, statusCol, cityCol;
    @FXML
    private TableColumn<ResidentialComplexDao, Long> housesCol;
    @FXML
    private TableColumn buttons;
    @FXML
    private ChoiceBox<String> statusFilter, cityFilter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        complexList = FXCollections.observableList(houseService.getAllComplex());
        filteredList = new FilteredList<>(complexList);
        statusFilter.setItems(FXCollections.observableArrayList("plan", "built", "building"));
        cityFilter.setItems(FXCollections.observableList(residentialComplexService.getAllCity()));

        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());
        cityFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());

        nameCol.setCellValueFactory(new PropertyValueFactory<ResidentialComplexDao, String>("name"));
        statusCol.setCellValueFactory(new PropertyValueFactory<ResidentialComplexDao, String>("status"));
        cityCol.setCellValueFactory(new PropertyValueFactory<ResidentialComplexDao, String>("city"));
        housesCol.setCellValueFactory(new PropertyValueFactory<ResidentialComplexDao, Long>("housesNumber"));


        Callback<TableColumn<ResidentialComplexDao, String>, TableCell<ResidentialComplexDao, String>> cellFactory = (param) -> {
            final TableCell<ResidentialComplexDao, String> cell = new TableCell<ResidentialComplexDao, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    }
                    else {
                        final Button editButton = new Button("Изменить");
                        editButton.setOnAction(event -> {
                            ResidentialComplexDao complexDao = getTableView().getItems().get(getIndex());
                            try {
                                ComplexController controller = (ComplexController) fxmlControllerUtils.changeScene(event, "complex.fxml");
                                controller.setComplex(residentialComplexService.getByName(complexDao.getName()));
                                controller.setText();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        setGraphic(editButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };

        buttons.setCellFactory(cellFactory);

        TableView.TableViewSelectionModel<ResidentialComplexDao> selectionModel = table.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if(newVal != null) {
                selectedComplex = newVal;
            }
        });
        table.setItems(filteredList);
    }

    public void filter() {
        filteredList.setPredicate(
                t ->  (t.getStatus().equals(statusFilter.getValue()) || statusFilter.getValue() == null) &&
                        (t.getCity().equals(cityFilter.getValue()) || cityFilter.getValue() == null)
        );
    }
    public void addComplex(ActionEvent event) throws IOException {
        ComplexController controller = (ComplexController) fxmlControllerUtils.changeScene(event, "complex.fxml");
        controller.setAddMode();
    }
    public void reset(ActionEvent event) {
        statusFilter.setValue(null);
        cityFilter.setValue(null);
    }
    public void goToHouses(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "houseList.fxml");
    }
    public void goToApartments(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "apartmentList.fxml");
    }
    public void goToReview(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "review.fxml");
    }
}
