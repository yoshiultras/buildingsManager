package com.yoshiultras.buildingsManager.controllers;

import com.yoshiultras.buildingsManager.StageReadyEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @Autowired
    ApplicationContext ac;
    @FXML
    Button button;

    public void toMain() {
        ac.publishEvent(new StageReadyEvent(new Stage()));
    }
}
