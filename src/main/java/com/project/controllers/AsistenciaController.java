package com.project.controllers;

import com.project.config.ServiceFactory;
import com.project.models.Asistencia;
import com.project.models.Empleado;
import com.project.services.AsistenciaService;
import com.project.services.EmpleadoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;

public class AsistenciaController {

    // Servicio de asistencia
    private AsistenciaService asistenciaService = ServiceFactory.getAsistenciaService();
    private EmpleadoService empleadoService = ServiceFactory.getEmpleadoService();

    // Elementos de la pantalla
    @FXML
    private TableView<Asistencia> tablaAsistencia;

    @FXML
    private TableColumn<Asistencia, Integer> colEmpleadoId;

    @FXML
    private TableColumn<Asistencia, String> colFecha;

    @FXML
    private TableColumn<Asistencia, String> colHoraEntrada;

    @FXML
    private TableColumn<Asistencia, String> colHoraSalida;

    @FXML
    private TableColumn<Asistencia, String> colEstado;

    @FXML
    private TableColumn<Asistencia, Integer> colMinutosTardanza;

    @FXML
    private ComboBox<Empleado> cmbEmpleado;

    @FXML
    private TextField txtMotivo;

    @FXML
    private Label lblMensaje;

    // Inicializar la tabla
    @FXML
    public void initialize() {
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<>("empleadoId"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colMinutosTardanza.setCellValueFactory(new PropertyValueFactory<>("minutosTardanza"));

        cargarEmpleados();

        // Cargar asistencias de hoy al abrir
        cargarAsistenciasDeHoy();
    }

    // Entrada
    @FXML
    public void registrarEntrada() {
        try {
            int empleadoId = obtenerEmpleadoIdSeleccionado();
            asistenciaService.registrarEntrada(empleadoId);
            mostrarMensaje("Entrada registrada correctamente");
            cargarAsistenciasDeHoy();
        } catch (RuntimeException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    // Salida
    @FXML
    public void registrarSalida() {
        try {
            int empleadoId = obtenerEmpleadoIdSeleccionado();
            asistenciaService.registrarSalida(empleadoId);
            mostrarMensaje("Salida registrada correctamente");
            cargarAsistenciasDeHoy();
        } catch (RuntimeException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    // Ver historial
    @FXML
    public void verHistorial() {
        try {
            int empleadoId = obtenerEmpleadoIdSeleccionado();
            List<Asistencia> historial = asistenciaService.obtenerHistorial(empleadoId);

            if (historial.isEmpty()) {
                mostrarMensaje("No hay registros para este empleado");
            } else {
                ObservableList<Asistencia> lista = FXCollections.observableArrayList(historial);
                tablaAsistencia.setItems(lista);
                mostrarMensaje("Historial cargado: " + historial.size() + " registros");
            }
        } catch (RuntimeException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    // Justificar asistencia
    @FXML
    public void justificarAsistencia() {
        Asistencia seleccionada = tablaAsistencia.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona un registro de la tabla");
            return;
        }

        String motivo = txtMotivo.getText().trim();
        if (motivo.isEmpty()) {
            mostrarMensaje("Ingresa un motivo para justificar");
            return;
        }

        try {
            asistenciaService.justificarAsistencia(seleccionada.getId(), motivo, 1);
            mostrarMensaje("Asistencia justificada correctamente");
            cargarAsistenciasDeHoy();
        } catch (RuntimeException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    private void cargarEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerEmpleados();
        cmbEmpleado.setItems(FXCollections.observableArrayList(empleados));

        cmbEmpleado.setConverter(new StringConverter<>() {
            @Override
            public String toString(Empleado empleado) {
                if (empleado == null) {
                    return "";
                }
                return empleado.getNombres() + " " + empleado.getApellidos();
            }

            @Override
            public Empleado fromString(String string) {
                return null;
            }
        });
    }

    private int obtenerEmpleadoIdSeleccionado() {
        Empleado empleado = cmbEmpleado.getValue();

        if (empleado == null) {
            throw new RuntimeException("Selecciona un empleado");
        }

        return empleado.getId();
    }
    // Cargar asistencias de hoy
    private void cargarAsistenciasDeHoy() {
        List<Asistencia> lista = asistenciaService.obtenerAsistenciasDeHoy();
        ObservableList<Asistencia> observableList = FXCollections.observableArrayList(lista);
        tablaAsistencia.setItems(observableList);
    }

    // Mostrar mensaje
    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }
}