package com.yoshiultras.buildingsManager;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages="com.yoshiultras.buildingsManager.repository")
@EnableAutoConfiguration
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class BootfulApplication {
    public static void main(String[] args) throws Exception {
        try {
            Application.launch(BuildingsManagerApplication.class, args);
        } catch (Exception e) {
            throw new Exception("Подключитесь к VPN Московского Политеха");
        }

    }
}
