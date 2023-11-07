package com.yoshiultras.buildingsManager.view;

import com.yoshiultras.buildingsManager.controller.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FXMLControllerUtils {

    @Autowired
    private ApplicationContext AC;

    public FXMLController changeScene(ActionEvent event, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        loader.setControllerFactory(AC::getBean);
        Parent root = loader.load();
        FXMLController controller = loader.getController();
        setView(event, root);
        return controller;
    }

    public void setView(ActionEvent event, Parent root) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
