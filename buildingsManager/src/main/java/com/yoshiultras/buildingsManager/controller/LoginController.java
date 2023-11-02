package com.yoshiultras.buildingsManager.controller;

import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController implements FXMLController {

    private final FXMLControllerUtils fxmlControllerUtils;

    @Autowired
    public LoginController(FXMLControllerUtils fxmlControllerUtils) {
        this.fxmlControllerUtils = fxmlControllerUtils;
    }

    @FXML
    private Button button;

    public void toMain(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "main.fxml");
    }
}
