package com.project.controllers;

import com.project.config.ServiceFactory;
import com.project.layout.BaseLayoutController;
import com.project.models.PerfilEmpleadoDTO;
import com.project.models.Usuario;
import com.project.services.AuthService;
import com.project.services.ImageService;
import com.project.services.PerfilService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class MiPerfilController {

    // Header
    @FXML private ImageView imgPerfil;
    @FXML private Label lblAvatarInicial;
    @FXML private Label lblNombreCompleto;
    @FXML private Label lblCargoArea;
    @FXML private Label lblRolBadge;
    @FXML private Label lblEstadoBadge;
    @FXML private Label lblUploadStatus;

    // Personal
    @FXML private Label lblCodigo;
    @FXML private Label lblDni;
    @FXML private Label lblFechaNacimiento;
    @FXML private Label lblFechaIngreso;
    @FXML private Label lblDiasVacaciones;

    // Contacto (Editables)
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEmail;

    // Laboral
    @FXML private Label lblSueldoBase;
    @FXML private Label lblTipoContrato;
    @FXML private Label lblSistemaPension;
    @FXML private Label lblFechaFinContrato;
    @FXML private Label lblUltimoAcceso;

    private final PerfilService perfilService = ServiceFactory.getPerfilService();
    private final ImageService  imageService  = ServiceFactory.getImageService();
    private PerfilEmpleadoDTO perfilActual;

    @FXML
    public void initialize() {
        Circle clip = new Circle(50, 50, 50);
        imgPerfil.setClip(clip);
        cargarDatos();
    }

    private void cargarDatos() {
        Integer empleadoId = com.project.common.util.SessionManager.getUsuarioLogueado().getEmpleadoId();
        
        Platform.runLater(() -> {
            try {
                perfilActual = perfilService.obtenerPerfilCompleto(empleadoId);
                poblarCampos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void poblarCampos() {
        if (perfilActual == null) return;

        // Header
        lblNombreCompleto.setText(perfilActual.getNombres() + " " + perfilActual.getApellidos());
        lblCargoArea.setText(perfilActual.getCargo() + " | " + perfilActual.getArea());
        lblRolBadge.setText(perfilActual.getRol());
        lblEstadoBadge.setText(perfilActual.getEstadoEmpleado());
        
        if (perfilActual.getFotoUrl() != null && !perfilActual.getFotoUrl().isEmpty()) {
            imgPerfil.setImage(new Image(perfilActual.getFotoUrl(), true));
            lblAvatarInicial.setVisible(false);
        } else {
            lblAvatarInicial.setText(perfilActual.getNombres().substring(0,1).toUpperCase());
            lblAvatarInicial.setVisible(true);
        }

        // Datos
        lblCodigo.setText(perfilActual.getCodigoEmpleado());
        lblDni.setText(perfilActual.getDni());
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblFechaNacimiento.setText(perfilActual.getFechaNacimiento().format(dtf));
        lblFechaIngreso.setText(perfilActual.getFechaIngreso().format(dtf));
        lblDiasVacaciones.setText(String.valueOf(perfilActual.getDiasVacacionesDisponibles()));

        txtTelefono.setText(perfilActual.getTelefono());
        txtDireccion.setText(perfilActual.getDireccion());
        txtEmail.setText(perfilActual.getEmail());

        lblSueldoBase.setText(String.format("S/ %.2f", perfilActual.getSueldoBase()));
        lblTipoContrato.setText(perfilActual.getTipoContrato());
        lblSistemaPension.setText(perfilActual.getSistemaPension());
        
        if (perfilActual.getFechaFinContrato() != null) {
            lblFechaFinContrato.setText(perfilActual.getFechaFinContrato().format(dtf));
        } else {
            lblFechaFinContrato.setText("Indefinido");
        }

        if (perfilActual.getUltimoAcceso() != null) {
            lblUltimoAcceso.setText(perfilActual.getUltimoAcceso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
    }

    @FXML
    private void guardarCambios() {
        try {
            perfilActual.setTelefono(txtTelefono.getText());
            perfilActual.setDireccion(txtDireccion.getText());
            // El email no es editable por el usuario en este flujo simplificado
            
            perfilService.actualizarPerfil(perfilActual);
            com.project.common.util.NotificacionService.exito("Perfil actualizado correctamente");
            
            // Actualizar sidebar/header notificando el cambio
            BaseLayoutController.notificarPerfilActualizado();
            
        } catch (Exception e) {
            com.project.common.util.NotificacionService.error("Error al actualizar perfil: " + e.getMessage());
        }
    }

    @FXML
    private void cambiarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen de Perfil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(imgPerfil.getScene().getWindow());
        if (selectedFile != null) {
            new Thread(() -> {
                try {
                    Platform.runLater(() -> lblUploadStatus.setText("Subiendo..."));
                    String url = imageService.subirImagen(selectedFile);
                    perfilActual.setFotoUrl(url);
                    perfilService.actualizarPerfil(perfilActual);
                    
                    Platform.runLater(() -> {
                        imgPerfil.setImage(new Image(url));
                        lblAvatarInicial.setVisible(false);
                        lblUploadStatus.setText("Foto actualizada");
                        BaseLayoutController.notificarPerfilActualizado();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        lblUploadStatus.setText("Error al subir");
                        com.project.common.util.NotificacionService.error("Error: " + e.getMessage());
                    });
                }
            }).start();
        }
    }
}
