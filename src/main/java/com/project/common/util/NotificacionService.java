package com.project.common.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

public class NotificacionService {

    public static void exito(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.INFORMATION, titulo, mensaje, "/com/project/css/notificaciones.css");
    }

    public static void exito(String mensaje) {
        exito("✓ Éxito", mensaje);
    }


    public static void advertencia(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.WARNING, titulo, mensaje, "/com/project/css/notificaciones.css");
    }

    public static void advertencia(String mensaje) {
        advertencia("⚠ Advertencia", mensaje);
    }


    public static void error(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.ERROR, titulo, mensaje, "/com/project/css/notificaciones.css");
    }

    public static void error(String mensaje) {
        error("✗ Error", mensaje);
    }

    public static void info(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.INFORMATION, titulo, mensaje, "/com/project/css/notificaciones.css");
    }

    public static void info(String mensaje) {
        info("ℹ Información", mensaje);
    }

    public static boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.getDialogPane().getStylesheets().add("/com/project/css/notificaciones.css");

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    public static boolean confirmar(String mensaje) {
        return confirmar("Confirmar acción", mensaje);
    }

    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje, String cssPath) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);

        try {
            String css = NotificacionService.class.getResource(cssPath).toExternalForm();
            alert.getDialogPane().getStylesheets().add(css);
        } catch (
                Exception e) {
            System.err.println("CSS no encontrado: " + cssPath);
        }

        alert.showAndWait();
    }
}