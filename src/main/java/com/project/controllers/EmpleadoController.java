package com.project.controllers;

import com.project.services.ImageService;
import com.project.services.impl.ImageServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class EmpleadoController {
    @FXML
    private ImageView fotoPreview;

    @FXML
    private Button btnSeleccionarFoto;

    @FXML
    private Button btnEliminarFoto;

    @FXML
    private Button btnSubirFoto;

    @FXML
    private Label statusLabel;

    private final ImageService imagenService;
    private File archivoSeleccionado;
    private String urlFotoSubida;

    public EmpleadoController() {
        this.imagenService = new ImageServiceImpl();
    }

    @FXML
    public void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto de empleado");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        archivoSeleccionado = fileChooser.showOpenDialog(btnSeleccionarFoto.getScene().getWindow());

        if (archivoSeleccionado != null) {
            try {
                Image preview = new Image(archivoSeleccionado.toURI().toString());
                fotoPreview.setImage(preview);
                statusLabel.setText("✓ Imagen seleccionada: " + archivoSeleccionado.getName());
                System.out.println("[INFO] Imagen seleccionada: " + archivoSeleccionado.getAbsolutePath());
            } catch (
                    Exception e) {
                statusLabel.setText("✗ Error al cargar preview: " + e.getMessage());
                System.out.println("[ERROR] Error al cargar preview: " + e.getMessage());
            }
        } else {
            statusLabel.setText("Selección cancelada");
        }
    }

    @FXML
    public void subirFoto() {
        if (archivoSeleccionado == null) {
            statusLabel.setText("✗ Selecciona una imagen primero");
            System.out.println("[WARN] No hay imagen seleccionada");
            return;
        }

        statusLabel.setText("⏳ Subiendo imagen...");

        new Thread(() -> {
            try {
                urlFotoSubida = imagenService.subirImagen(archivoSeleccionado);

                // Mostrar URL en consola
                System.out.println("════════════════════════════════════════");
                System.out.println("[SUCCESS] Imagen subida exitosamente");
                System.out.println("════════════════════════════════════════");
                System.out.println("URL Final: " + urlFotoSubida);
                System.out.println("════════════════════════════════════════");

                // Actualizar UI en thread principal
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("✓ URL: " + urlFotoSubida);
                });

            } catch (
                    IllegalArgumentException e) {
                System.out.println("[ERROR] Validación: " + e.getMessage());
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("✗ Error: " + e.getMessage());
                });
            } catch (
                    Exception e) {
                System.out.println("[ERROR] Error al subir: " + e.getMessage());
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("✗ Error al subir: " + e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    public void eliminarFoto() {
        fotoPreview.setImage(null);
        archivoSeleccionado = null;
        urlFotoSubida = null;
        statusLabel.setText("Selecciona una imagen...");
        System.out.println("[INFO] Imagen y URL eliminadas");
    }
}
