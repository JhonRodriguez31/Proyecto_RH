package com.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        MainApplication.class.getResource("/com/project/css/styles.css")
                ).toExternalForm()
        );

        stage.setTitle("Sistema de Recursos Humanos");
        stage.setScene(scene);
//        stage.setMinWidth(1000);
//        stage.setMinHeight(700);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
