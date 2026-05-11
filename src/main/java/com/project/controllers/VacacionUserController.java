package com.project.controllers;

import com.project.common.util.NotificacionService;
import com.project.models.Empleado;
import com.project.models.Vacacion;
import com.project.services.EmpleadoService;
import com.project.services.VacacionService;
import com.project.services.impl.EmpleadoServiceImpl;
import com.project.services.impl.VacacionServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller de vacaciones — vista EMPLEADO (USER).
 * El empleado ve sus propias solicitudes y puede crear nuevas.
 */
public class VacacionUserController implements Initializable {

    // TODO: reemplazar con el ID del empleado logueado (sesión)
    private static final Integer EMPLEADO_ID_SESION = 1;

    private final VacacionService  vacacionService  = new VacacionServiceImpl();
    private final EmpleadoService  empleadoService  = new EmpleadoServiceImpl();

    private Empleado empleadoActual;

    // ── Tabla de mis solicitudes ──────────────────────────────
    @FXML private TableView<Vacacion>           tablaMisVacaciones;
    @FXML private TableColumn<Vacacion, String> colFechaSolicitud;
    @FXML private TableColumn<Vacacion, String> colFechaInicio;
    @FXML private TableColumn<Vacacion, String> colFechaFin;
    @FXML private TableColumn<Vacacion, String> colDias;
    @FXML private TableColumn<Vacacion, String> colEstado;
    @FXML private TableColumn<Vacacion, String> colObservacion;

    // ── Formulario nueva solicitud ────────────────────────────
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private TextArea   txObservacion;
    @FXML private Label      lblDiasCalculados;
    @FXML private Label      lblDiasDisponibles;
    @FXML private Button     btnSolicitar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarEmpleado();
        configurarColumnas();
        cargarMisVacaciones();
        configurarCalculoAutomatico();
    }

    // ── Datos ─────────────────────────────────────────────────

    private void cargarEmpleado() {
        try {
            empleadoActual = empleadoService.obtenerEmpleado(EMPLEADO_ID_SESION);
            int disponibles = empleadoActual.getDiasVacacionesDisponibles() != null
                    ? empleadoActual.getDiasVacacionesDisponibles() : 0;
            lblDiasDisponibles.setText("Días disponibles: " + disponibles);
        } catch (Exception e) {
            NotificacionService.error("Error al cargar datos del empleado.");
        }
    }

    private void cargarMisVacaciones() {
        try {
            List<Vacacion> lista = vacacionService.listarPorEmpleado(EMPLEADO_ID_SESION);
            tablaMisVacaciones.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            NotificacionService.error("Error al cargar tus solicitudes: " + e.getMessage());
        }
    }

    // ── Columnas ──────────────────────────────────────────────

    private void configurarColumnas() {
        colFechaSolicitud.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getFechaSolicitud() != null
                                ? c.getValue().getFechaSolicitud().toString() : ""));
        colFechaInicio.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getFechaInicio() != null
                                ? c.getValue().getFechaInicio().toString() : ""));
        colFechaFin.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getFechaFin() != null
                                ? c.getValue().getFechaFin().toString() : ""));
        colDias.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getDiasSolicitados())));
        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEstado()));
        colObservacion.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getObservacion() != null ? c.getValue().getObservacion() : ""));

        // Colorear estado
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) { setText(null); setStyle(""); return; }
                setText(estado);
                setStyle(switch (estado) {
                    case "APROBADA"  -> "-fx-text-fill: #16A34A; -fx-font-weight: bold;";
                    case "RECHAZADA" -> "-fx-text-fill: #DC2626; -fx-font-weight: bold;";
                    default          -> "-fx-text-fill: #D97706; -fx-font-weight: bold;";
                });
            }
        });
    }

    // ── Cálculo automático de días al elegir fechas ───────────

    private void configurarCalculoAutomatico() {
        dpFechaFin.valueProperty().addListener((obs, a, b) -> actualizarDiasCalculados());
        dpFechaInicio.valueProperty().addListener((obs, a, b) -> actualizarDiasCalculados());
    }

    private void actualizarDiasCalculados() {
        LocalDate ini = dpFechaInicio.getValue();
        LocalDate fin = dpFechaFin.getValue();
        if (ini != null && fin != null && !fin.isBefore(ini)) {
            long dias = ChronoUnit.DAYS.between(ini, fin) + 1;
            lblDiasCalculados.setText("Días a solicitar: " + dias);
        } else {
            lblDiasCalculados.setText("Días a solicitar: --");
        }
    }

    // ── Acción: solicitar ─────────────────────────────────────

    @FXML
    private void solicitarVacacion() {
        LocalDate ini = dpFechaInicio.getValue();
        LocalDate fin = dpFechaFin.getValue();

        if (ini == null || fin == null) {
            NotificacionService.advertencia("Debes seleccionar las fechas de inicio y fin.");
            return;
        }

        Vacacion vacacion = new Vacacion();
        vacacion.setEmpleadoId(EMPLEADO_ID_SESION);
        vacacion.setFechaInicio(ini);
        vacacion.setFechaFin(fin);
        vacacion.setObservacion(txObservacion.getText().trim());

        try {
            vacacionService.solicitar(vacacion, empleadoActual);
            NotificacionService.exito("Solicitud enviada. Queda PENDIENTE de aprobación.");
            dpFechaInicio.setValue(null);
            dpFechaFin.setValue(null);
            txObservacion.clear();
            lblDiasCalculados.setText("Días a solicitar: --");
            cargarMisVacaciones();
            cargarEmpleado(); // refresca días disponibles
        } catch (IllegalArgumentException e) {
            NotificacionService.advertencia(e.getMessage());
        } catch (Exception e) {
            NotificacionService.error("Error al enviar solicitud: " + e.getMessage());
        }
    }

    @FXML
    private void recargar() {
        cargarMisVacaciones();
        cargarEmpleado();
    }
}
