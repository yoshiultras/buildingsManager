package com.yoshiultras.buildingsManager;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class BootfulApplication {
    public static void main(String[] args) {
        Application.launch(BuildingsManagerApplication.class, args);
    }
}
