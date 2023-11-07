package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.dao.ApartmentDao;
import com.yoshiultras.buildingsManager.service.ApartmentService;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.utils.ApartmentUtils;
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
public class ApartmentListController implements FXMLController, Initializable {
    private ApartmentDao selectedApartment;
    private List<ApartmentDao> apartmentList;
    private FilteredList<ApartmentDao> filteredList;
    private FilteredList<String> filteredHouses;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private ApartmentUtils apartmentUtils;
    @Autowired
    private FXMLControllerUtils fxmlControllerUtils;

    private TableView<ApartmentDao> table = new TableView<>();

    private TableColumn<ApartmentDao, String> complex = new TableColumn<>("ЖК"), address = new TableColumn<>("Адрес"), status = new TableColumn<>("Статус");

    private TableColumn<ApartmentDao, Double> square = new TableColumn<>("Площадь");

    private TableColumn<ApartmentDao, Integer> rooms = new TableColumn<>("Комнаты"), entrance = new TableColumn<>("Подъезд"), storey = new TableColumn<>("Этаж");

    private TableColumn buttons = new TableColumn<>();
    @FXML
    private ChoiceBox<String> complexFilter, houseFilter, statusFilter;
    @FXML
    private TextField storeyFilter, entranceFilter, searchFilter;
    @FXML
    private Pagination pagination;
    @FXML
    private Label errorLabel;
    private final static int rowsPerPage = 12;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apartmentList = apartmentService.getAllDao();
        filteredList = new FilteredList<>(FXCollections.observableList(apartmentList));

        List<String> complexes = new ArrayList<>();
        List<String> houses = new ArrayList<>();
        for (ApartmentDao apartmentDao : apartmentList) {
            if (!complexes.contains(apartmentDao.getComplex())) complexes.add(apartmentDao.getComplex());
            if (!houses.contains(apartmentDao.getAddress().split(", ")[1])) houses.add(apartmentDao.getAddress().split(", ")[1]);
        }
        complexFilter.setItems(FXCollections.observableList(complexes));
        filteredHouses = new FilteredList<>(FXCollections.observableList(houses));
        houseFilter.setItems(filteredHouses);
        statusFilter.setItems(FXCollections.observableArrayList("sold", "ready"));

        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());
        complexFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());
        complexFilter.valueProperty().addListener((observable, oldValue, newValue) -> houseFilter());
        houseFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());
        entranceFilter.textProperty().addListener((observable, oldValue, newValue) -> filter());
        storeyFilter.textProperty().addListener((observable, oldValue, newValue) -> filter());

        if (table.getColumns().size() == 0) {
            complex.setCellValueFactory(new PropertyValueFactory<ApartmentDao, String>("complex"));
            address.setCellValueFactory(new PropertyValueFactory<ApartmentDao, String>("address"));
            square.setCellValueFactory(new PropertyValueFactory<ApartmentDao, Double>("square"));
            rooms.setCellValueFactory(new PropertyValueFactory<ApartmentDao, Integer>("rooms"));
            entrance.setCellValueFactory(new PropertyValueFactory<ApartmentDao, Integer>("entrance"));
            storey.setCellValueFactory(new PropertyValueFactory<ApartmentDao, Integer>("storey"));
            status.setCellValueFactory(new PropertyValueFactory<ApartmentDao, String>("statusSale"));

            complex.setPrefWidth(143);
            address.setPrefWidth(173);
            square.setPrefWidth(63);
            rooms.setPrefWidth(54);
            entrance.setPrefWidth(64);
            storey.setPrefWidth(41);
            status.setPrefWidth(74);
            buttons.setPrefWidth(77);




            Callback<TableColumn<ApartmentDao, String>, TableCell<ApartmentDao, String>> cellFactory = (param) -> {
                final TableCell<ApartmentDao, String> cell = new TableCell<ApartmentDao, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        }
                        else {
                            final Button editButton = new Button("Изменить");
                            editButton.setOnAction(event -> {
                                ApartmentDao apartmentDao = getTableView().getItems().get(getIndex());
                                try {
                                    ApartmentController controller = (ApartmentController) fxmlControllerUtils.changeScene(event, "apartment.fxml");
                                    controller.setApartment(apartmentService.getById(apartmentDao.getId()).orElseThrow());
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

            TableView.TableViewSelectionModel<ApartmentDao> selectionModel = table.getSelectionModel();
            selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
                if(newVal != null) {
                    selectedApartment = newVal;
                }
            });


            table.getColumns().addAll(complex, address, square, rooms, entrance, storey, status, buttons);
            for (TableColumn<ApartmentDao, ?> column : table.getColumns()) {
                column.setResizable(false);
                column.setSortable(false);
            }
        }

        pagination.setPageFactory(this::createPage);
        pagination.setPageCount((filteredList.size() / rowsPerPage) + (filteredList.size() % rowsPerPage == 0 ? 0 : 1));
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, filteredList.size());
        table.setItems(FXCollections.observableArrayList(filteredList.subList(fromIndex, toIndex)));
        return table;
    }

    public void addApartment(ActionEvent event) throws IOException {
        ApartmentController controller = (ApartmentController) fxmlControllerUtils.changeScene(event, "apartment.fxml");
        controller.setAddMode();
    }

    public void houseFilter() {
        houseFilter.setValue(null);
        filteredHouses.setPredicate(
                t -> houseService.getComplexNameByHouseNumber(t).equals(complexFilter.getValue()) || complexFilter.getValue() == null
        );
    }
    public void filter() {
        try {
            errorLabel.setVisible(false);
            filteredList.setPredicate(
                    t -> (complexFilter.getValue() == null || t.getComplex().equals(complexFilter.getValue())) &&
                            (houseFilter.getValue() == null || t.getAddress().split(", ")[1].equals(houseFilter.getValue())) &&
                            (statusFilter.getValue() == null || t.getStatusSale().equals(statusFilter.getValue())) &&
                            (entranceFilter.getText().equals("") || t.getEntrance() == Integer.parseInt(entranceFilter.getText())) &&
                            (storeyFilter.getText().equals("") || t.getStorey() == Integer.parseInt(storeyFilter.getText()))
            );
            pagination.setPageCount((filteredList.size() / rowsPerPage) + (filteredList.size() % rowsPerPage == 0 ? 0 : 1));
        } catch (Exception e) {
            errorLabel.setVisible(true);
        }
    }
    public void reset(ActionEvent event) {
        complexFilter.setValue(null);
        houseFilter.setValue(null);
        statusFilter.setValue(null);
        entranceFilter.setText("");
        storeyFilter.setText("");
    }
    public void goToComplexes(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "complexList.fxml");
    }
    public void goToHouses(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "houseList.fxml");
    }
    public void find(ActionEvent event) {
        reset(event);
        filteredList.setPredicate(
                t -> apartmentUtils.isValid(t.getAddress(), searchFilter.getText()) ||
                        apartmentUtils.isValid(t.getComplex(), searchFilter.getText()) ||
                        apartmentUtils.isValid(t.getComplex()+" "+t.getAddress(), searchFilter.getText())
        );
        pagination.setPageCount((filteredList.size() / rowsPerPage) + (filteredList.size() % rowsPerPage == 0 ? 0 : 1));
    }
    public void goToReview(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "review.fxml");
    }
}
