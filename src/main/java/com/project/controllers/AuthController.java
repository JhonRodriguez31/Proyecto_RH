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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class AuthController {

    @FXML
    private HBox authRoot;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField visiblePasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button resetPasswordBtn;
    @FXML
    private Button togglePasswordBtn;
    @FXML
    private FontIcon togglePasswordIcon;
    @FXML
    private HBox errorBox;
    @FXML
    private Label errorLabel;

    private boolean passwordVisible = false;

    @FXML
    public void initialize() {
        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleLogin());
        visiblePasswordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = getPassword().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor, completa todos los campos");
            return;
        }

        boolean isAuthenticated = authenticate(username, password);

        if (isAuthenticated) {
            hideError();
            boolean isAdmin = "admin".equalsIgnoreCase(username);
            navigateToLayout(isAdmin);
        } else {
            showError("Usuario o contraseña incorrectos");
            clearPassword();
            if (passwordVisible) {
                visiblePasswordField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
        }
    }

    @FXML
    private void handleResetPassword() {
        navigateToResetPassword();
    }

    @FXML
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            visiblePasswordField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            visiblePasswordField.requestFocus();
            visiblePasswordField.positionCaret(visiblePasswordField.getText().length());
            togglePasswordIcon.setIconLiteral("mdi2e-eye-off-outline");
        } else {
            passwordField.setText(visiblePasswordField.getText());
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
            togglePasswordIcon.setIconLiteral("mdi2e-eye-outline");
        }
    }


    private String getPassword() {
        return passwordVisible ? visiblePasswordField.getText() : passwordField.getText();
    }

    private void clearPassword() {
        passwordField.clear();
        visiblePasswordField.clear();
    }

    private boolean authenticate(String username, String password) {
        // TODO: Replace with real DB authentication via services layer
        return ("admin".equalsIgnoreCase(username) && "admin123".equals(password))
                || ("user".equalsIgnoreCase(username) && "user123".equals(password));
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

    private void navigateToLayout(boolean isAdmin) {
        try {
            String layoutPath = isAdmin
                    ? "/com/project/fxml/layout/admin-layout.fxml"
                    : "/com/project/fxml/layout/user-layout.fxml";

            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource(layoutPath)
            );
            Parent newRoot = loader.load();
            crossfadeTo(newRoot, "Sistema de Recursos Humanos");
        } catch (
                IOException e) {
            showError("Error al cargar el sistema: " + e.getMessage());
        }
    }

    private void navigateToResetPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/com/project/fxml/auth/reset-password.fxml")
            );
            Parent newRoot = loader.load();
            crossfadeTo(newRoot, "Sistema de Recursos Humanos — Restablecer Contraseña");
        } catch (
                IOException e) {
            showError("Error al cargar la pantalla: " + e.getMessage());
        }
    }

    private void crossfadeTo(Parent newRoot, String title) {
        Scene scene = authRoot.getScene();
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
