package com.yoshiultras.buildingsManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;

@Component
public class MainController {

    @Autowired
    ApplicationContext ac;

    @FXML
    Button button;
    @FXML
    Label label;


    public void go() {
//        ac.publishEvent(new StageReadyEvent(new Stage()));
    }
}
