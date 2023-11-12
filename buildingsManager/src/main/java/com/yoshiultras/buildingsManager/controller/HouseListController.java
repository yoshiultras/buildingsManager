package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.dao.HouseDao;
import com.yoshiultras.buildingsManager.dao.ResidentialComplexDao;
import com.yoshiultras.buildingsManager.model.House;
import com.yoshiultras.buildingsManager.model.ResidentialComplex;
import com.yoshiultras.buildingsManager.service.HouseService;
import com.yoshiultras.buildingsManager.service.ResidentialComplexService;
import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Component
public class HouseListController implements FXMLController, Initializable {
    private HouseDao selectedHouse;
    private ObservableList<HouseDao> houseList;
    private FilteredList<HouseDao> filteredList;
    private List<ResidentialComplex> complexList;
    @Autowired
    private HouseService houseService;
    @Autowired
    private ResidentialComplexService residentialComplexService;
    @Autowired
    private FXMLControllerUtils fxmlControllerUtils;
    @FXML
    private TableView<HouseDao> table;
    @FXML
    private TableColumn<HouseDao, String> complex, street, number, status;
    @FXML
    private TableColumn<HouseDao, Integer> sold, ready;
    @FXML
    private TableColumn buttons;
    @FXML
    private Button resetButton;
    @FXML
    private TextField streetFilter;
    @FXML
    private ChoiceBox<ResidentialComplex> complexFilter;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        houseList = FXCollections.observableList(houseService.getHousesDao());
        filteredList = new FilteredList<>(houseList);

        complexList = residentialComplexService.getAll();
        complexFilter.setItems(FXCollections.observableList(complexList));


        streetFilter.textProperty().addListener((observable, oldValue, newValue) -> filter());

        complexFilter.valueProperty().addListener((observable, oldValue, newValue) -> filter());

        complex.setCellValueFactory(new PropertyValueFactory<HouseDao, String>("complex"));
        street.setCellValueFactory(new PropertyValueFactory<HouseDao, String>("street"));
        number.setCellValueFactory(new PropertyValueFactory<HouseDao, String>("number"));
        status.setCellValueFactory(new PropertyValueFactory<HouseDao, String>("status"));
        sold.setCellValueFactory(new PropertyValueFactory<HouseDao, Integer>("apartmentsSold"));
        ready.setCellValueFactory(new PropertyValueFactory<HouseDao, Integer>("apartmentsReady"));

        Callback<TableColumn<HouseDao, String>, TableCell<HouseDao, String>> cellFactory = (param) -> {
            final TableCell<HouseDao, String> cell = new TableCell<HouseDao, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    }
                    else {
                        final Button editButton = new Button("Изменить");
                        editButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 156, 26), CornerRadii.EMPTY, null)));
                        editButton.setOnAction(event -> {
                            HouseDao houseDao = getTableView().getItems().get(getIndex());
                            try {
                                HouseController controller = (HouseController) fxmlControllerUtils.changeScene(event, "house.fxml");
                                controller.setHouse(houseService.getHouseByNumber(houseDao.getNumber()));
                                controller.setComplex(complexList);
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

        TableView.TableViewSelectionModel<HouseDao> selectionModel = table.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if(newVal != null) {
                selectedHouse = newVal;
            }
        });
        table.setItems(filteredList);
    }

    public void filter() {
        filteredList.setPredicate(
                t ->  (t.getStreet().toLowerCase().matches(streetFilter.getText().toLowerCase()+".*") || streetFilter.getText().equals("")) &&
                        (complexFilter.getValue() == null || t.getComplex().equals(complexFilter.getValue().getName()))
        );
    }

    public void reset(ActionEvent event) {
        streetFilter.setText("");
        complexFilter.setValue(null);
    }

    public void addHouse(ActionEvent event) throws IOException {
        HouseController controller = (HouseController) fxmlControllerUtils.changeScene(event, "house.fxml");
        controller.setAddMode();
        controller.setComplex(complexList);
    }
    public void goToComplexes(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "complexList.fxml");
    }
    public void goToApartments(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "apartmentList.fxml");
    }
    public void goToReview(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "review.fxml");
    }
}
