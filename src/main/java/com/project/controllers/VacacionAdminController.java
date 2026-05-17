package com.project.controllers;

import com.project.common.util.NotificacionService;
import com.project.config.ServiceFactory;
import com.project.models.Vacacion;
import com.project.services.VacacionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller de vacaciones — vista ADMIN.
 * El admin puede ver todas las solicitudes y aprobar / rechazar.
 */



public class VacacionAdminController implements Initializable {
	

    private static final Integer ADMIN_ID = 1; // TODO: reemplazar con ID del usuario logueado

    private final VacacionService vacacionService = ServiceFactory.getVacacionService();

    @FXML private TableView<Vacacion>            tablaVacaciones;
    @FXML private TableColumn<Vacacion, String>  colEmpleado;
    @FXML private TableColumn<Vacacion, String>  colFechaSolicitud;
    @FXML private TableColumn<Vacacion, String>  colFechaInicio;
    @FXML private TableColumn<Vacacion, String>  colFechaFin;
    @FXML private TableColumn<Vacacion, String>  colDias;
    @FXML private TableColumn<Vacacion, String>  colEstado;
    @FXML private TableColumn<Vacacion, String>  colObservacion;

    @FXML private Button btnAprobar;
    @FXML private Button btnRechazar;
    @FXML private Button btnRecargar;

    // ── Filtro de estado ──────────────────────────────────────
    @FXML private ComboBox<String> cbFiltroEstado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        configurarFiltro();
        cargarVacaciones();

        // Activar botones solo si hay selección
        tablaPlanillas().getSelectionModel().selectedItemProperty().addListener(
                (obs, a, sel) -> {
                    boolean haySeleccion = sel != null;
                    boolean esPendiente  = haySeleccion && "PENDIENTE".equals(sel.getEstado());
                    btnAprobar.setDisable(!esPendiente);
                    btnRechazar.setDisable(!esPendiente);
                });
    }

    // ── Columnas ──────────────────────────────────────────────

    private void configurarColumnas() {
        colEmpleado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombreEmpleado()));
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
                String style;
                switch (estado) {
                    case "APROBADA":
                        style = "-fx-text-fill: #16A34A; -fx-font-weight: bold;";
                        break;
                    case "RECHAZADA":
                        style = "-fx-text-fill: #DC2626; -fx-font-weight: bold;";
                        break;
                    default:
                        style = "-fx-text-fill: #D97706; -fx-font-weight: bold;";
                        break;
                }
                setStyle(style);
            }
        });
    }

    private void configurarFiltro() {
        cbFiltroEstado.setItems(FXCollections.observableArrayList(
                "TODOS", "PENDIENTE", "APROBADA", "RECHAZADA"));
        cbFiltroEstado.setValue("TODOS");
        cbFiltroEstado.setOnAction(e -> filtrar());
    }

    // ── Datos ─────────────────────────────────────────────────

    private List<Vacacion> todasLasVacaciones;

    private void cargarVacaciones() {
        try {
            todasLasVacaciones = vacacionService.listar();
            tablaVacaciones.setItems(FXCollections.observableArrayList(todasLasVacaciones));
        } catch (Exception e) {
            NotificacionService.error("Error al cargar vacaciones: " + e.getMessage());
        }
    }

    private void filtrar() {
        String filtro = cbFiltroEstado.getValue();
        if ("TODOS".equals(filtro) || filtro == null) {
            tablaVacaciones.setItems(FXCollections.observableArrayList(todasLasVacaciones));
        } else {
            var filtradas = todasLasVacaciones.stream()
                    .filter(v -> filtro.equals(v.getEstado()))
                    .toList();
            tablaVacaciones.setItems(FXCollections.observableArrayList(filtradas));
        }
    }

    // ── Acciones ──────────────────────────────────────────────

    @FXML
    private void aprobarVacacion() {
        Vacacion sel = tablaVacaciones.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        boolean ok = NotificacionService.confirmar(
                "¿Aprobar la solicitud de " + sel.getNombreEmpleado()
                + " por " + sel.getDiasSolicitados() + " días?");
        if (!ok) return;

        try {
            vacacionService.aprobar(sel.getId(), ADMIN_ID);
            NotificacionService.exito("Vacación aprobada correctamente.");
            cargarVacaciones();
        } catch (Exception e) {
            NotificacionService.error("Error al aprobar: " + e.getMessage());
        }
    }

    @FXML
    private void rechazarVacacion() {
        Vacacion sel = tablaVacaciones.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        // Pedir motivo al admin
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rechazar solicitud");
        dialog.setHeaderText("Ingresa el motivo del rechazo");
        dialog.setContentText("Motivo:");

        dialog.showAndWait().ifPresent(motivo -> {
            try {
                vacacionService.rechazar(sel.getId(), ADMIN_ID, motivo);
                NotificacionService.exito("Solicitud rechazada.");
                cargarVacaciones();
            } catch (IllegalArgumentException e) {
                NotificacionService.advertencia(e.getMessage());
            } catch (Exception e) {
                NotificacionService.error("Error al rechazar: " + e.getMessage());
            }
        });
    }

    @FXML
    private void recargar() {
        cargarVacaciones();
    }

    // Helper: alias para no confundir con nombre de tabla
    private TableView<Vacacion> tablaPlanillas() {
        return tablaVacaciones;
    }
}
