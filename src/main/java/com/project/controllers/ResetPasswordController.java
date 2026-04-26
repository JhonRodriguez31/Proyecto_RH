package com.project.controllers;

import com.project.MainApplication;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ResetPasswordController {

    @FXML
    private HBox resetRoot;
    @FXML
    private TextField usernameField;
    @FXML
    private Button resetButton;
    @FXML
    private Button backToLoginBtn;
    @FXML
    private HBox successBox;
    @FXML
    private Label successLabel;
    @FXML
    private HBox errorBox;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        usernameField.setOnAction(e -> handleReset());
    }

    @FXML
    private void handleReset() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            hideSuccess();
            showError("Por favor, ingresa tu nombre de usuario");
            return;
        }

        hideError();
        showSuccess("Se han enviado las instrucciones de restablecimiento para el usuario \"" + username + "\"");
    }

    @FXML
    private void handleBackToLogin() {
        navigateToLogin();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorBox.setVisible(true);
        errorBox.setManaged(true);

        FadeTransition fade = new FadeTransition(Duration.millis(200), errorBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_OUT);
        fade.play();
    }

    private void hideError() {
        errorBox.setVisible(false);
        errorBox.setManaged(false);
    }

    private void showSuccess(String message) {
        successLabel.setText(message);
        successBox.setVisible(true);
        successBox.setManaged(true);

        FadeTransition fade = new FadeTransition(Duration.millis(200), successBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_OUT);
        fade.play();
    }

    private void hideSuccess() {
        successBox.setVisible(false);
        successBox.setManaged(false);
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/com/project/fxml/auth/login.fxml")
            );
            Parent newRoot = loader.load();
            crossfadeTo(newRoot, "Sistema de Recursos Humanos — Iniciar Sesión");
        } catch (
                IOException e) {
            showError("Error al cargar la pantalla: " + e.getMessage());
        }
    }

    private void crossfadeTo(Parent newRoot, String title) {
        Scene scene = resetRoot.getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent oldRoot = scene.getRoot();

        newRoot.setOpacity(0);
        StackPane wrapper = new StackPane(oldRoot, newRoot);
        scene.setRoot(wrapper);
        stage.setTitle(title);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), oldRoot);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setInterpolator(Interpolator.EASE_IN);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition crossfade = new ParallelTransition(fadeOut, fadeIn);
        crossfade.setOnFinished(e -> {
            wrapper.getChildren().clear();
            scene.setRoot(newRoot);
        });
        crossfade.play();
    }
}
