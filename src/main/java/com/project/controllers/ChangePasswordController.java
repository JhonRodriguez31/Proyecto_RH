package com.project.controllers;

import com.project.MainApplication;
import com.project.config.ServiceFactory;
import com.project.models.Usuario;
import com.project.services.AuthService;
import com.project.common.util.SessionManager;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ChangePasswordController {

    private final AuthService authService = ServiceFactory.getAuthService();

    @FXML private HBox changeRoot;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button changeBtn;
    @FXML private HBox errorBox;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        newPasswordField.setOnAction(e -> confirmPasswordField.requestFocus());
        confirmPasswordField.setOnAction(e -> handleChangePassword());
    }

    @FXML
    private void handleChangePassword() {
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            showError("Por favor, completa ambos campos.");
            return;
        }

        if (newPass.length() < 8) {
            showError("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showError("Las contraseñas no coinciden.");
            return;
        }

        try {
            Usuario usuario = SessionManager.getUsuarioLogueado();
            if (usuario == null) {
                showError("No se pudo identificar al usuario.");
                return;
            }

            authService.cambiarContraseña(usuario.getId(), newPass);
            
            // Refrescar estado en sesión
            usuario.setPrimeraVez(false);
            
            // Navegar al dashboard
            boolean isAdmin = "admin".equalsIgnoreCase(usuario.getRole().toString());
            navigateToLayout(isAdmin);
            
        } catch (Exception e) {
            showError("Error al actualizar: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorBox.setVisible(true);
        errorBox.setManaged(true);
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
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error al cargar el sistema: " + e.getMessage());
        }
    }

    private void crossfadeTo(Parent newRoot, String title) {
        Scene scene = changeRoot.getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent oldRoot = scene.getRoot();

        newRoot.setOpacity(0);
        StackPane wrapper = new StackPane(oldRoot, newRoot);
        scene.setRoot(wrapper);
        stage.setTitle(title);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), oldRoot);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition crossfade = new ParallelTransition(fadeOut, fadeIn);
        crossfade.setOnFinished(e -> {
            wrapper.getChildren().clear();
            scene.setRoot(newRoot);
        });
        crossfade.play();
    }
}
