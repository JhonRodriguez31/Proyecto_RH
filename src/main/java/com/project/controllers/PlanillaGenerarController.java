package com.project.controllers;

import com.project.DAO.impl.EmpleadoDaoImpl;
import com.project.common.util.NotificacionService;
import com.project.models.Empleado;
import com.project.models.Planilla;
import com.project.models.PlanillaDetalle;
import com.project.services.EmpleadoService;
import com.project.services.impl.EmpleadoServiceImpl;
import com.project.services.impl.PlanillaServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class PlanillaGenerarController implements Initializable {
	
	private final EmpleadoService empleadoService =
	        new EmpleadoServiceImpl(new EmpleadoDaoImpl());

    private final PlanillaServiceImpl planillaService = new PlanillaServiceImpl();

    // ── Selección ─────────────────────────────────────────────
    @FXML private ComboBox<Empleado> cbEmpleado;
    @FXML private ComboBox<String>   cbMes;
    @FXML private ComboBox<String>   cbAnio;
    @FXML private TextField          txtMinutosTardanza;
    @FXML private TextField          txtDiasFalta;

    // ── Tabla de previsualización ─────────────────────────────
    @FXML private TableView<PlanillaDetalle> tablaDetalle;
    @FXML private TableColumn<PlanillaDetalle, String> colConcepto;
    @FXML private TableColumn<PlanillaDetalle, String> colTipo;
    @FXML private TableColumn<PlanillaDetalle, String> colDescripcion;
    @FXML private TableColumn<PlanillaDetalle, String> colMonto;

    // ── Resumen ───────────────────────────────────────────────
    @FXML private Label lblIngresos;
    @FXML private Label lblDescuentos;
    @FXML private Label lblNeto;
    @FXML private Button btnGuardar;

    private List<PlanillaDetalle> detallesPreview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarEmpleados();
        cargarPeriodos();
        configurarColumnas();
        btnGuardar.setDisable(true);

        txtMinutosTardanza.setText("0");
        txtDiasFalta.setText("0");
    }

    // ── Carga de combos ───────────────────────────────────────

    private void cargarEmpleados() {
        try {
            List<Empleado> empleados = empleadoService.obtenerEmpleados();
            cbEmpleado.setItems(FXCollections.observableArrayList(empleados));
            // Mostrar nombre completo en el combo
            cbEmpleado.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Empleado e, boolean empty) {
                    super.updateItem(e, empty);
                    setText(empty || e == null ? null : e.getNombres() + " " + e.getApellidos());
                }
            });
            cbEmpleado.setButtonCell(cbEmpleado.getCellFactory().call(null));
        } catch (Exception e) {
            NotificacionService.error("Error al cargar empleados: " + e.getMessage());
        }
    }

    private void cargarPeriodos() {
        cbMes.setItems(FXCollections.observableArrayList(
                "01","02","03","04","05","06","07","08","09","10","11","12"));
        cbMes.setValue(String.format("%02d", LocalDate.now().getMonthValue()));

        int anioActual = LocalDate.now().getYear();
        cbAnio.setItems(FXCollections.observableArrayList(
                String.valueOf(anioActual - 1),
                String.valueOf(anioActual),
                String.valueOf(anioActual + 1)));
        cbAnio.setValue(String.valueOf(anioActual));
    }

    // ── Columnas ──────────────────────────────────────────────

    private void configurarColumnas() {
        colConcepto.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getConceptoNombre()));
        colTipo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getConceptoTipo()));
        colDescripcion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescripcion()));
        colMonto.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("S/ %.2f",
                        Math.abs(c.getValue().getMonto()))));

        // Color rojo/verde según tipo
        colMonto.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setText(null); setStyle(""); return; }
                setText(val);
                PlanillaDetalle d = getTableView().getItems().get(getIndex());
                setStyle(d.esIngreso()
                        ? "-fx-text-fill: #16A34A; -fx-font-weight: bold;"
                        : "-fx-text-fill: #DC2626; -fx-font-weight: bold;");
            }
        });
    }

    // ── Calcular (previsualizar) ──────────────────────────────

    @FXML
    private void calcular() {
        Empleado empleado = cbEmpleado.getValue();
        if (empleado == null) {
            NotificacionService.advertencia("Selecciona un empleado."); return;
        }
        if (cbMes.getValue() == null || cbAnio.getValue() == null) {
            NotificacionService.advertencia("Selecciona el periodo."); return;
        }

        int minutos = parsearEntero(txtMinutosTardanza.getText(), "Minutos tardanza");
        int faltas  = parsearEntero(txtDiasFalta.getText(), "Días falta");
        if (minutos < 0 || faltas < 0) return;

        try {
            detallesPreview = planillaService.calcularDetallesPreview(
                    empleado.getId(), minutos, faltas);

            tablaDetalle.setItems(FXCollections.observableArrayList(detallesPreview));

            double ingresos   = detallesPreview.stream().filter(PlanillaDetalle::esIngreso)
                                               .mapToDouble(PlanillaDetalle::getMonto).sum();
            double descuentos = detallesPreview.stream().filter(d -> !d.esIngreso())
                                               .mapToDouble(d -> Math.abs(d.getMonto())).sum();
            double neto = ingresos - descuentos;

            lblIngresos.setText(String.format("S/ %.2f", ingresos));
            lblDescuentos.setText(String.format("S/ %.2f", descuentos));
            lblNeto.setText(String.format("S/ %.2f", neto));

            btnGuardar.setDisable(false);

        } catch (IllegalStateException e) {
            NotificacionService.advertencia(e.getMessage());
        } catch (Exception e) {
            NotificacionService.error("Error al calcular: " + e.getMessage());
        }
    }

    // ── Guardar ───────────────────────────────────────────────

    @FXML
    private void guardarPlanilla() {

        if (detallesPreview == null || detallesPreview.isEmpty()) {
            NotificacionService.advertencia("Primero calcula la planilla.");
            return;
        }

        String periodo = cbAnio.getValue() + "-" + cbMes.getValue();
        Empleado empleado = cbEmpleado.getValue();

        Planilla planilla = new Planilla();
        planilla.setEmpleado_id(empleado.getId());
        planilla.setPeriodo(periodo);

        System.out.println("Empleado: " + planilla.getEmpleado_id());
        System.out.println("Periodo: " + planilla.getPeriodo());

        try {
        	
        	int minutos = parsearEntero(txtMinutosTardanza.getText(), "Minutos tardanza");
        	int faltas = parsearEntero(txtDiasFalta.getText(), "Días falta");

        	planillaService.generarPlanilla(planilla, minutos, faltas, 1);

        	NotificacionService.exito("Planilla generada correctamente para el periodo " + periodo);
        	cerrar();

            /*// 👇 AQUÍ VA TU DEBUG + LLAMADA
            Integer planillaId = planillaService.registrarPlanilla(planilla, detallesPreview, 1);

            System.out.println("PLANILLA ID DEVUELTO: " + planillaId);

            if (planillaId == null) {
                throw new RuntimeException("No se generó ID de planilla");
            }

            NotificacionService.exito("Planilla generada correctamente para el periodo " + periodo);
            cerrar();*/

        } catch (Exception e) {
            NotificacionService.error("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    // ── Utilidad ──────────────────────────────────────────────

    private int parsearEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            NotificacionService.advertencia(campo + " debe ser un número entero.");
            return -1;
        }
    }
}
