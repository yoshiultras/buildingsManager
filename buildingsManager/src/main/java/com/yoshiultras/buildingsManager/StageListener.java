package com.yoshiultras.buildingsManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private final String APPLICATION_TITLE;
    private final int WIDTH;
    private final int HEIGHT;
    private final Resource FXML;
    private final ApplicationContext AC;

    public StageListener(@Value("${spring.application.title}") String applicationTitle,
                         @Value("classpath:/main.fxml") Resource resource,
                         @Value("${spring.application.width}") int width,
                         @Value("${spring.application.height}") int height,
                         ApplicationContext ac) {
        this.APPLICATION_TITLE = applicationTitle;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.FXML = resource;
        this.AC = ac;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            URL url = this.FXML.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(AC::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, HEIGHT, WIDTH);
            stage.setScene(scene);
            stage.setTitle(this.APPLICATION_TITLE);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
