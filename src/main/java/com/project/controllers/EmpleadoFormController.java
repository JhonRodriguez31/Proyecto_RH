package com.project.controllers;

import com.project.common.enums.Estado;
import com.project.config.ServiceFactory;
import com.project.models.Empleado;
import com.project.services.EmpleadoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmpleadoFormController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDni;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private ComboBox<Estado> cbEstado;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private DatePicker dpFechaIngreso;
    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;

    // Image Upload Fields
    @FXML private ImageView imgPerfil;
    @FXML private Label lblAvatarInicial;
    @FXML private Label lblUploadStatus;
    @FXML private Button btnCambiarFoto;

    private final EmpleadoService empleadoService = ServiceFactory.getEmpleadoService();
    private Empleado empleado;
    private boolean guardado = false;
    private java.io.File archivoFotoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEstado.setItems(FXCollections.observableArrayList(Estado.values()));
        cbEstado.setValue(Estado.ACTIVO);

        btnCancelar.setOnAction(e -> cerrarVentana());
        btnGuardar.setOnAction(e -> guardar());

        // Configurar clip circular para el avatar
        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(50, 50, 50);
        imgPerfil.setClip(clip);
        
        // Cargar imagen por defecto inicialmente
        cargarFotoPerfil(Empleado.DEFAULT_PHOTO_URL);

        // Actualizar avatar inicial cuando cambian nombres
        txtNombres.textProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null && !nuevo.isEmpty() && archivoFotoSeleccionado == null && 
                (empleado == null || empleado.getFotoUrl() == null || empleado.getFotoUrl().equals(Empleado.DEFAULT_PHOTO_URL))) {
                lblAvatarInicial.setText(nuevo.substring(0, 1).toUpperCase());
                lblAvatarInicial.setVisible(true);
            }
        });

        // Valores por defecto
        dpFechaIngreso.setValue(LocalDate.now());
        dpFechaNacimiento.setValue(LocalDate.now().minusYears(20));

        // Auto-generar código de empleado
        String siguienteCodigo = empleadoService.generarSiguienteCodigo();
        txtCodigo.setText(siguienteCodigo);
        txtCodigo.setEditable(false);

        // ── Restricciones peruanas ──

        // DNI: solo 8 dígitos numéricos
        txtDni.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,8}")) {
                return change;
            }
            return null;
        }));
        txtDni.setPromptText("Ej: 71234567");

        // Teléfono: solo 9 dígitos numéricos (celular peruano)
        txtTelefono.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,9}")) {
                return change;
            }
            return null;
        }));
        txtTelefono.setPromptText("Ej: 987654321");

        // Nombres y Apellidos: solo letras y espacios
        txtNombres.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                return change;
            }
            return null;
        }));

        txtApellidos.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                return change;
            }
            return null;
        }));
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        if (empleado != null) {
            titleLabel.setText("Editar Empleado");
            txtCodigo.setText(empleado.getCodigoEmpleado());
            txtCodigo.setEditable(false);

            // Temporarily remove formatters to set existing values
            txtDni.setTextFormatter(null);
            txtTelefono.setTextFormatter(null);
            txtNombres.setTextFormatter(null);
            txtApellidos.setTextFormatter(null);

            txtDni.setText(empleado.getDni());
            txtNombres.setText(empleado.getNombres());
            txtApellidos.setText(empleado.getApellidos());
            txtTelefono.setText(empleado.getTelefono());
            txtDireccion.setText(empleado.getDireccion());
            cbEstado.setValue(empleado.getEstado());
            dpFechaNacimiento.setValue(empleado.getFechaNacimiento());
            dpFechaIngreso.setValue(empleado.getFechaIngreso());

            // Re-apply formatters
            txtDni.setTextFormatter(new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d{0,8}")) return change;
                return null;
            }));
            txtTelefono.setTextFormatter(new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d{0,9}")) return change;
                return null;
            }));
            txtNombres.setTextFormatter(new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) return change;
                return null;
            }));
            txtApellidos.setTextFormatter(new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) return change;
                return null;
            }));

            // En edición, el DNI no se edita
            txtDni.setEditable(false);

            if (empleado.getFotoUrl() != null) {
                cargarFotoPerfil(empleado.getFotoUrl());
            }
        }
    }

    private void cargarFotoPerfil(String url) {
        if (url != null && !url.trim().isEmpty()) {
            try {
                Image image = new Image(url, true);
                imgPerfil.setImage(image);
                lblAvatarInicial.setVisible(false);
            } catch (Exception e) {
                lblAvatarInicial.setVisible(true);
            }
        }
    }

    @FXML
    public void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto del empleado");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        archivoFotoSeleccionado = fileChooser.showOpenDialog(
                imgPerfil.getScene().getWindow()
        );

        if (archivoFotoSeleccionado != null) {
            Image preview = new Image(archivoFotoSeleccionado.toURI().toString());
            imgPerfil.setImage(preview);
            lblAvatarInicial.setVisible(false);
            lblUploadStatus.setText("✓ " + archivoFotoSeleccionado.getName());
            lblUploadStatus.setStyle("-fx-text-fill: #86efac;");
        }
    }

    private void guardar() {
        if (!validarCampos()) return;

        if (empleado == null) {
            empleado = new Empleado();
        }

        empleado.setCodigoEmpleado(txtCodigo.getText());
        empleado.setDni(txtDni.getText());
        empleado.setNombres(txtNombres.getText().trim());
        empleado.setApellidos(txtApellidos.getText().trim());
        empleado.setTelefono(txtTelefono.getText());
        empleado.setDireccion(txtDireccion.getText());
        empleado.setEstado(cbEstado.getValue());
        empleado.setFechaNacimiento(dpFechaNacimiento.getValue());
        empleado.setFechaIngreso(dpFechaIngreso.getValue());

        if (empleado.getDiasVacacionesDisponibles() == null) {
            empleado.setDiasVacacionesDisponibles(0);
        }

        guardado = true;
        cerrarVentana();
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtDni.getText().isEmpty() || txtDni.getText().length() != 8) {
            errores.append("• El DNI debe tener exactamente 8 dígitos.\n");
        }

        if (txtNombres.getText().trim().isEmpty()) {
            errores.append("• El nombre es obligatorio.\n");
        }

        if (txtApellidos.getText().trim().isEmpty()) {
            errores.append("• El apellido es obligatorio.\n");
        }

        if (txtTelefono.getText().isEmpty() || txtTelefono.getText().length() != 9) {
            errores.append("• El teléfono debe tener exactamente 9 dígitos.\n");
        } else if (!txtTelefono.getText().startsWith("9")) {
            errores.append("• El teléfono celular debe empezar con 9.\n");
        }

        if (dpFechaNacimiento.getValue() == null) {
            errores.append("• La fecha de nacimiento es obligatoria.\n");
        } else if (dpFechaNacimiento.getValue().isAfter(LocalDate.now().minusYears(18))) {
            errores.append("• El empleado debe ser mayor de 18 años.\n");
        }

        if (dpFechaIngreso.getValue() == null) {
            errores.append("• La fecha de ingreso es obligatoria.\n");
        }

        if (txtDireccion.getText().trim().isEmpty()) {
            errores.append("• La dirección es obligatoria.\n");
        }

        if (errores.length() > 0) {
            mostrarAlerta("Errores de Validación", errores.toString());
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText("Por favor corrija los siguientes errores:");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public File getArchivoFotoSeleccionado() {
        return archivoFotoSeleccionado;
    }
}
