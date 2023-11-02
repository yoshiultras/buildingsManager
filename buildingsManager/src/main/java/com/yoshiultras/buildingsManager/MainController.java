package com.yoshiultras.buildingsManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
