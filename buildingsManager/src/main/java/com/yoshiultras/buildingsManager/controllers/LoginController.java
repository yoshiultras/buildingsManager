package com.yoshiultras.buildingsManager.controllers;

import com.yoshiultras.buildingsManager.view.FXMLControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController implements FXMLController {
    @Autowired
    FXMLControllerUtils fxmlControllerUtils;
    @FXML
    Button button;

    public void toMain(ActionEvent event) throws IOException {
        fxmlControllerUtils.changeScene(event, "main.fxml");
    }
}
