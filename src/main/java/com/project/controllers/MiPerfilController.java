package com.project.controllers;

import com.project.config.ServiceFactory;
import com.project.layout.BaseLayoutController;
import com.project.models.PerfilEmpleadoDTO;
import com.project.models.Usuario;
import com.project.services.AuthService;
import com.project.services.ImageService;
import com.project.services.PerfilService;
import com.project.services.impl.ImageServiceImpl;
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

    // Account
    @FXML private Label lblUsername;
    @FXML private Label lblEmail;
    @FXML private Label lblUltimoAcceso;

    // Contact (editable)
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;

    // Employment
    @FXML private Label lblCargo;
    @FXML private Label lblArea;
    @FXML private Label lblTipoContrato;
    @FXML private Label lblSueldoBase;
    @FXML private Label lblSistemaPension;
    @FXML private Label lblInicioContrato;
    @FXML private Label lblFinContrato;

    // Actions
    @FXML private Label lblEstadoGuardado;

    private final PerfilService perfilService = ServiceFactory.getPerfilService();
    private final AuthService authService = ServiceFactory.getAuthService();
    private final ImageService imageService = new ImageServiceImpl();

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private PerfilEmpleadoDTO perfilActual;
    private File archivoFotoSeleccionado;
    private String nuevaFotoUrl;

    @FXML
    public void initialize() {
        cargarPerfil();
    }

    private void cargarPerfil() {
        Usuario usuario = authService.obtenerUsuarioAutenticado();
        if (usuario == null || usuario.getEmpleadoId() == null) {
            lblNombreCompleto.setText("No hay sesión activa");
            return;
        }

        perfilActual = perfilService.obtenerPerfil(usuario.getEmpleadoId());
        if (perfilActual == null) {
            lblNombreCompleto.setText("Perfil no encontrado");
            return;
        }

        poblarDatos(perfilActual);
    }

    private void poblarDatos(PerfilEmpleadoDTO p) {
        // Header
        lblNombreCompleto.setText(p.getNombreCompleto());
        lblCargoArea.setText(p.getCargoArea());
        lblRolBadge.setText(p.getRol() != null ? p.getRol() : "—");
        lblEstadoBadge.setText(p.getEstadoEmpleado() != null ? p.getEstadoEmpleado() : "—");

        // Avatar
        if (p.getNombres() != null && !p.getNombres().isEmpty()) {
            lblAvatarInicial.setText(p.getNombres().substring(0, 1).toUpperCase());
        }
        cargarFotoPerfil(p.getFotoUrl());

        // Personal
        lblCodigo.setText(valOr(p.getCodigoEmpleado()));
        lblDni.setText(valOr(p.getDni()));
        lblFechaNacimiento.setText(p.getFechaNacimiento() != null ? p.getFechaNacimiento().format(DATE_FMT) : "—");
        lblFechaIngreso.setText(p.getFechaIngreso() != null ? p.getFechaIngreso().format(DATE_FMT) : "—");
        lblDiasVacaciones.setText(p.getDiasVacacionesDisponibles() != null ? String.valueOf(p.getDiasVacacionesDisponibles()) : "—");

        // Account
        lblUsername.setText(valOr(p.getUsername()));
        lblEmail.setText(valOr(p.getEmail()));
        lblUltimoAcceso.setText(p.getUltimoAcceso() != null ? p.getUltimoAcceso().format(DATETIME_FMT) : "Sin registro");

        // Contact (editable)
        txtTelefono.setText(p.getTelefono() != null ? p.getTelefono() : "");
        txtDireccion.setText(p.getDireccion() != null ? p.getDireccion() : "");

        // Employment
        lblCargo.setText(valOr(p.getCargo()));
        lblArea.setText(valOr(p.getArea()));
        lblTipoContrato.setText(valOr(p.getTipoContrato()));
        lblSueldoBase.setText(p.getSueldoBase() != null ? String.format("S/ %.2f", p.getSueldoBase()) : "—");
        lblSistemaPension.setText(valOr(p.getSistemaPension()));
        lblInicioContrato.setText(p.getFechaInicioContrato() != null ? p.getFechaInicioContrato().format(DATE_FMT) : "—");
        lblFinContrato.setText(p.getFechaFinContrato() != null ? p.getFechaFinContrato().format(DATE_FMT) : "Indefinido");
    }

    private void cargarFotoPerfil(String url) {
        if (url != null && !url.trim().isEmpty()) {
            try {
                Image image = new Image(url, true);
                imgPerfil.setImage(image);
                Circle clip = new Circle(55, 55, 55);
                imgPerfil.setClip(clip);
                lblAvatarInicial.setVisible(false);
            } catch (Exception e) {
                lblAvatarInicial.setVisible(true);
            }
        }
    }

    @FXML
    public void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto de perfil");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        archivoFotoSeleccionado = fileChooser.showOpenDialog(
                imgPerfil.getScene().getWindow()
        );

        if (archivoFotoSeleccionado != null) {
            // Preview local
            Image preview = new Image(archivoFotoSeleccionado.toURI().toString());
            imgPerfil.setImage(preview);
            Circle clip = new Circle(55, 55, 55);
            imgPerfil.setClip(clip);
            lblAvatarInicial.setVisible(false);
            lblUploadStatus.setText("✓ " + archivoFotoSeleccionado.getName());
            lblUploadStatus.setStyle("-fx-text-fill: #86efac;");
        }
    }

    @FXML
    public void guardarCambios() {
        if (perfilActual == null) return;

        lblEstadoGuardado.setText("Guardando...");
        lblEstadoGuardado.getStyleClass().setAll("status-label-success");

        new Thread(() -> {
            try {
                // Si hay foto nueva, subirla primero
                String fotoUrl = perfilActual.getFotoUrl();
                if (archivoFotoSeleccionado != null) {
                    lblUploadStatusUpdate("⏳ Subiendo imagen...");
                    fotoUrl = imageService.subirImagen(archivoFotoSeleccionado);
                    nuevaFotoUrl = fotoUrl;
                    lblUploadStatusUpdate("✓ Imagen subida");
                }

                String telefono = txtTelefono.getText().trim();
                String direccion = txtDireccion.getText().trim();

                perfilService.actualizarPerfil(
                        perfilActual.getEmpleadoId(),
                        telefono,
                        direccion,
                        fotoUrl
                );

                // Actualizar estado local
                perfilActual.setTelefono(telefono);
                perfilActual.setDireccion(direccion);
                if (nuevaFotoUrl != null) {
                    perfilActual.setFotoUrl(nuevaFotoUrl);
                }
                archivoFotoSeleccionado = null;

                // Notificar al layout para que refresque el avatar del sidebar
                BaseLayoutController.notificarPerfilActualizado();

                Platform.runLater(() -> {
                    lblEstadoGuardado.setText("✓ Cambios guardados correctamente");
                    lblEstadoGuardado.getStyleClass().setAll("status-label-success");
                });

            } catch (Exception e) {
                // NUNCA tragarse una excepción sin loguearla
                System.err.println("════════════════════════════════════════");
                System.err.println("[ERROR] Fallo al guardar perfil:");
                System.err.println("  Tipo: " + e.getClass().getName());
                System.err.println("  Mensaje: " + e.getMessage());
                if (e.getCause() != null) {
                    System.err.println("  Causa: " + e.getCause().getClass().getName() + " → " + e.getCause().getMessage());
                }
                System.err.println("════════════════════════════════════════");
                e.printStackTrace();

                String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                if (e.getCause() != null && e.getCause().getMessage() != null) {
                    errorMsg = e.getCause().getMessage();
                }
                String finalMsg = errorMsg;
                Platform.runLater(() -> {
                    lblEstadoGuardado.setText("✗ Error: " + finalMsg);
                    lblEstadoGuardado.getStyleClass().setAll("status-label-error");
                });
            }
        }).start();
    }

    private void lblUploadStatusUpdate(String text) {
        Platform.runLater(() -> lblUploadStatus.setText(text));
    }

    private String valOr(String value) {
        return value != null && !value.isEmpty() ? value : "—";
    }
}
