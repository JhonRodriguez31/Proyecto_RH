package com.project.controllers;

import com.project.common.enums.Estado;
import com.project.models.Empleado;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

    private Empleado empleado;
    private boolean guardado = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEstado.setItems(FXCollections.observableArrayList(Estado.values()));
        cbEstado.setValue(Estado.ACTIVO);
        
        btnCancelar.setOnAction(e -> cerrarVentana());
        btnGuardar.setOnAction(e -> guardar());
        
        // Valores por defecto
        dpFechaIngreso.setValue(LocalDate.now());
        dpFechaNacimiento.setValue(LocalDate.now().minusYears(20));
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        if (empleado != null) {
            titleLabel.setText("Editar Empleado");
            txtCodigo.setText(empleado.getCodigoEmpleado());
            txtDni.setText(empleado.getDni());
            txtNombres.setText(empleado.getNombres());
            txtApellidos.setText(empleado.getApellidos());
            txtTelefono.setText(empleado.getTelefono());
            txtDireccion.setText(empleado.getDireccion());
            cbEstado.setValue(empleado.getEstado());
            dpFechaNacimiento.setValue(empleado.getFechaNacimiento());
            dpFechaIngreso.setValue(empleado.getFechaIngreso());
            
            // El código y DNI no se deberían editar si ya existen en algunos sistemas, 
            // pero lo dejaremos habilitado por ahora a menos que sea necesario.
            txtCodigo.setEditable(false); 
        }
    }

    private void guardar() {
        if (!validarCampos()) return;

        if (empleado == null) {
            empleado = new Empleado();
        }

        empleado.setCodigoEmpleado(txtCodigo.getText());
        empleado.setDni(txtDni.getText());
        empleado.setNombres(txtNombres.getText());
        empleado.setApellidos(txtApellidos.getText());
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
        if (txtCodigo.getText().isEmpty() || txtDni.getText().isEmpty() || 
            txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty()) {
            mostrarAlerta("Campos Obligatorios", "Por favor complete los campos principales.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
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
}
