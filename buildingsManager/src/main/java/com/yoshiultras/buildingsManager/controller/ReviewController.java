package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.dao.ApartmentDao;
import com.yoshiultras.buildingsManager.model.Apartment;
import com.yoshiultras.buildingsManager.service.ApartmentService;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.utils.ReviewUtils;
import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
public class ReviewController implements FXMLController, Initializable {

    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private FXMLControllerUtils fxmlControllerUtils;
    @Autowired
    private ReviewUtils reviewUtils;
    private List<ApartmentDao> apartmentList;
    private FilteredList<ApartmentDao> filteredList;
    private FilteredList<String> filteredHouses;
    private ApartmentDao selectedApartment;
    private final static int rowsPerPage = 9;

    private TableView<ApartmentDao> table = new TableView<>();
    private TableColumn<ApartmentDao, String> status = new TableColumn<>("Статус");
    private TableColumn<ApartmentDao, Integer> storey = new TableColumn<>("Этаж"), cost = new TableColumn<>("Стоимость"), number = new TableColumn<>("Номер квартиры");

    @FXML
    private Label label1, label2, label3, label4, label5, errorLabel;
    @FXML
    private Pagination pagination;
    @FXML
    private ChoiceBox<String> complexFilter, houseFilter;
    @FXML
    private TextField squareCost, roomCost, addedValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apartmentList = apartmentService.getAllDao();
        filteredList = new FilteredList<>(FXCollections.observableList(apartmentList));
        if (table.getColumns().size() == 0) {

            storey.setCellValueFactory(new PropertyValueFactory<ApartmentDao, Integer>("storey"));
            status.setCellValueFactory(new PropertyValueFactory<ApartmentDao, String>("statusSale"));
            number.setCellValueFactory(new PropertyValueFactory<ApartmentDao, Integer>("apartmentNumber"));
            cost.setCellValueFactory(new PropertyValueFactory<ApartmentDao, Integer>("costApartmentConstruction"));

            storey.setPrefWidth(173);
            status.setPrefWidth(173);
            number.setPrefWidth(173);
            cost.setPrefWidth(174);

            TableView.TableViewSelectionModel<ApartmentDao> selectionModel = table.getSelectionModel();
            selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
                if(newVal != null) {
                    selectedApartment = newVal;
                }
            });

            table.getColumns().addAll(number, storey, status, cost);
            for (TableColumn<ApartmentDao, ?> column : table.getColumns()) {
                column.setResizable(false);
                column.setSortable(false);
            }
        }

        List<String> complexes = new ArrayList<>();
        List<String> houses = new ArrayList<>();
        for (ApartmentDao apartment : apartmentList) {
            if (!complexes.contains(apartment.getComplex())) complexes.add(apartment.getComplex());
            if (!houses.contains(apartment.getAddress().split(", ")[1])) houses.add(apartment.getAddress().split(", ")[1]);
        }

        complexFilter.setItems(FXCollections.observableList(complexes));
        filteredHouses = new FilteredList<>(FXCollections.observableList(houses));
        houseFilter.setItems(filteredHouses);

        complexFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());
        complexFilter.valueProperty().addListener((observable, oldValue, newValue) -> houseFilter());
        houseFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());

        pagination.setPageFactory(this::createPage);
        pagination.setPageCount((filteredList.size() / rowsPerPage) + (filteredList.size() % rowsPerPage == 0 ? 0 : 1));

        squareCost.setText("90000");
        roomCost.setText("900000");
        addedValue.setText("1000000");
    }

    public void houseFilter() {
        houseFilter.setValue(null);
        filteredHouses.setPredicate(
                t -> houseService.getComplexNameByHouseNumber(t).equals(complexFilter.getValue())
        );
    }
    public void filter() {
        filteredList.setPredicate(
                t ->  (complexFilter.getValue() == null || t.getComplex().equals(complexFilter.getValue())) &&
                        (houseFilter.getValue() == null || t.getAddress().split(", ")[1].equals(houseFilter.getValue()))
        );
        pagination.setPageCount((filteredList.size() / rowsPerPage) + (filteredList.size() % rowsPerPage == 0 ? 0 : 1));
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, filteredList.size());
        table.setItems(FXCollections.observableArrayList(filteredList.subList(fromIndex, toIndex)));
        return table;
    }

    public void calculate(ActionEvent event) {
        try {
            errorLabel.setVisible(false);
            label1.setText(String.format("%.3f", reviewUtils.getValue(filteredList, Double.parseDouble(squareCost.getText()), Double.parseDouble(roomCost.getText()), Double.parseDouble(addedValue.getText()))));
            label2.setText(String.format("%.3f", reviewUtils.getValueSold(filteredList, Double.parseDouble(squareCost.getText()), Double.parseDouble(roomCost.getText()), Double.parseDouble(addedValue.getText()))));
            label3.setText(String.format("%.3f", reviewUtils.getValueUnsold(filteredList, Double.parseDouble(squareCost.getText()), Double.parseDouble(roomCost.getText()), Double.parseDouble(addedValue.getText()))));
            label4.setText(String.format("%.3f", reviewUtils.getCost(filteredList, complexFilter.getValue(), houseFilter.getValue())));
            label5.setText(String.format("%.3f", Double.parseDouble(label1.getText().replaceAll(",", "\\.")) - Double.parseDouble(label4.getText().replaceAll(",", "\\."))));
        } catch (Exception e) {
            errorLabel.setVisible(true);
            label1.setText("");
            label2.setText("");
            label3.setText("");
            label4.setText("");
            label5.setText("");
        }
    }

    public void reset(ActionEvent event) {
        complexFilter.setValue(null);
        houseFilter.setValue(null);
    }

    public void goToComplexes(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "complexList.fxml");
    }
    public void goToHouses(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "houseList.fxml");
    }
    public void goToApartments(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "apartmentList.fxml");
    }
}
