package com.project.controllers;

import com.project.common.util.NotificacionService;
import com.project.config.ServiceFactory;
import com.project.models.Planilla;
import com.project.models.PlanillaDetalle;
import com.project.services.PlanillaService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

public class PlanillaPrincipalController implements Initializable {

    private final PlanillaService planillaService = ServiceFactory.getPlanillaService();
    private boolean isLoading = false;

    // ── Tabla principal ───────────────────────────────────────
    @FXML private TableView<Planilla>            tablaPlanillas;
    @FXML private TableColumn<Planilla, String>  colEmpleado;
    @FXML private TableColumn<Planilla, String>  colPeriodo;
    @FXML private TableColumn<Planilla, String>  colFecha;
    @FXML private TableColumn<Planilla, String>  colEstado;
    @FXML private TableColumn<Planilla, String>  colSueldo;
    @FXML private TableColumn<Planilla, String> colAcciones;

    // ── Filtros ───────────────────────────────────────────────
    @FXML private ComboBox<String> cbPeriodo;
    @FXML private ComboBox<String> cbEmpleado;
    @FXML private ComboBox<String> cbEstado;

    // ── Tabla INGRESOS ────────────────────────────────────────
    @FXML private TableView<PlanillaDetalle>           tablaIngresos;
    @FXML private TableColumn<PlanillaDetalle, String> colIngConcepto;
    @FXML private TableColumn<PlanillaDetalle, String> colIngDescripcion;
    @FXML private TableColumn<PlanillaDetalle, String> colIngMonto;

    // ── Tabla DESCUENTOS ──────────────────────────────────────
    @FXML private TableView<PlanillaDetalle>           tablaDescuentos;
    @FXML private TableColumn<PlanillaDetalle, String> colDescConcepto;
    @FXML private TableColumn<PlanillaDetalle, String> colDescDescripcion;
    @FXML private TableColumn<PlanillaDetalle, String> colDescMonto;

    // ── Totales / Badge ───────────────────────────────────────
    @FXML private Label lblTotalIngresos;
    @FXML private Label lblTotalDescuentos;
    @FXML private Label lblNeto;
    @FXML private Label lblBadgePlanilla;

    private List<Planilla> todasLasPlanillas = List.of();

    // ─────────────────────────────────────────────────────────

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnasPrincipal();
        configurarColumnasIngresos();
        configurarColumnasDescuentos();
        cargarPlanillas();

        tablaPlanillas.getSelectionModel().selectedItemProperty().addListener(
                (obs, ant, sel) -> {
                    if (sel != null) mostrarDetalle(sel);
                    else             limpiarDetalle();
                });
    }

    // ── Columnas tabla principal ──────────────────────────────

    private void configurarColumnasPrincipal() {
        colEmpleado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombreEmpleado()));
        colPeriodo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPeriodo()));
        colFecha.setCellValueFactory(c -> {
            String f = c.getValue().getFecha_generacion();
            return new SimpleStringProperty(f != null && f.length() >= 10 ? f.substring(0, 10) : "");
        });
        colSueldo.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getSueldoBase() != null
                                ? String.format("S/ %.2f", c.getValue().getSueldoBase()) : "-"));
        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEstado()));

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
        
    
    colAcciones.setCellFactory(col -> new TableCell<>() {
        @Override
        protected void updateItem(String val, boolean empty) {
            super.updateItem(val, empty);
            if (empty) { setGraphic(null); return; }

            Planilla planilla = getTableView().getItems().get(getIndex());
            
            MenuButton menu = new MenuButton("⋮ Acciones");
            menu.setMaxWidth(Double.MAX_VALUE);
            menu.setPrefWidth(this.getTableColumn().getWidth() - 10);
            menu.setStyle("-fx-background-color:#F3F4F6; -fx-border-color:#D1D5DB; -fx-background-radius:6;");

            MenuItem editar   = new MenuItem("✏ Editar");
            MenuItem eliminar = new MenuItem("🗑 Eliminar");

         editar.setOnAction(e ->   editarPlanilla(planilla));
            eliminar.setOnAction(e -> eliminarPlanillaSeleccionada());

            menu.getItems().addAll(editar, eliminar);
            setGraphic(menu);
        }
    });
    
    }

    // ── Columnas INGRESOS ─────────────────────────────────────

    private void configurarColumnasIngresos() {
        colIngConcepto.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getConceptoNombre()));
        colIngDescripcion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescripcion()));
        colIngMonto.setCellValueFactory(c ->
                new SimpleStringProperty(
                        String.format("+S/ %.2f", Math.abs(c.getValue().getMonto()))));

        colIngMonto.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setText(null); setStyle(""); return; }
                setText(val);
                setStyle("-fx-text-fill:#16A34A; -fx-font-weight:bold;");
            }
        });

        tablaIngresos.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(PlanillaDetalle d, boolean empty) {
                super.updateItem(d, empty);
                setStyle(empty || d == null ? "" : "-fx-background-color:#F0FDF4;");
            }
        });
    }

    // ── Columnas DESCUENTOS ───────────────────────────────────

    private void configurarColumnasDescuentos() {
        colDescConcepto.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getConceptoNombre()));
        colDescDescripcion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescripcion()));
        colDescMonto.setCellValueFactory(c ->
                new SimpleStringProperty(
                        String.format("-S/ %.2f", Math.abs(c.getValue().getMonto()))));

        colDescMonto.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setText(null); setStyle(""); return; }
                setText(val);
                setStyle("-fx-text-fill:#DC2626; -fx-font-weight:bold;");
            }
        });

        tablaDescuentos.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(PlanillaDetalle d, boolean empty) {
                super.updateItem(d, empty);
                setStyle(empty || d == null ? "" : "-fx-background-color:#FFF7F7;");
            }
        });
    }

    // ── Carga de datos ────────────────────────────────────────

    private void cargarPlanillas() {
        if (isLoading) return;
        isLoading = true;

        Task<List<Planilla>> task = new Task<>() {
            @Override
            protected List<Planilla> call() throws Exception {
                return planillaService.listarPlanillas();
            }
        };

        task.setOnSucceeded(e -> {
            todasLasPlanillas = task.getValue();
            poblarCombos(todasLasPlanillas);
            tablaPlanillas.setItems(FXCollections.observableArrayList(todasLasPlanillas));
            limpiarDetalle();
            isLoading = false;
        });

        task.setOnFailed(e -> {
            NotificacionService.error("Error al cargar planillas: " + task.getException().getMessage());
            isLoading = false;
        });

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void poblarCombos(List<Planilla> planillas) {
        List<String> periodos = planillas.stream()
                .map(Planilla::getPeriodo).distinct()
                .sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList());
        periodos.add(0, "Todos");
        cbPeriodo.setItems(FXCollections.observableArrayList(periodos));
        cbPeriodo.setValue("Todos");

        List<String> empleados = planillas.stream()
                .map(Planilla::getNombreEmpleado).distinct()
                .sorted().collect(Collectors.toList());
        empleados.add(0, "Todos");
        cbEmpleado.setItems(FXCollections.observableArrayList(empleados));
        cbEmpleado.setValue("Todos");

        cbEstado.setItems(FXCollections.observableArrayList(
                "Todos", "GENERADA", "PAGADA", "ANULADA"));
        cbEstado.setValue("Todos");
    }

    // ── Filtrado ──────────────────────────────────────────────

    @FXML
    private void aplicarFiltros() {
        String fp = cbPeriodo.getValue();
        String fe = cbEmpleado.getValue();
        String fs = cbEstado.getValue();

        List<Planilla> filtradas = todasLasPlanillas.stream()
                .filter(p -> fp == null || "Todos".equals(fp) || fp.equals(p.getPeriodo()))
                .filter(p -> fe == null || "Todos".equals(fe) || fe.equals(p.getNombreEmpleado()))
                .filter(p -> fs == null || "Todos".equals(fs) || fs.equals(p.getEstado()))
                .collect(Collectors.toList());

        tablaPlanillas.setItems(FXCollections.observableArrayList(filtradas));
        limpiarDetalle();
    }

    @FXML
    private void limpiarFiltros() {
        cbPeriodo.setValue("Todos");
        cbEmpleado.setValue("Todos");
        cbEstado.setValue("Todos");
        tablaPlanillas.setItems(FXCollections.observableArrayList(todasLasPlanillas));
        limpiarDetalle();
    }

    // ── Detalle separado por sección ──────────────────────────

    private void mostrarDetalle(Planilla planilla) {
        Task<List<PlanillaDetalle>> task = new Task<>() {
            @Override
            protected List<PlanillaDetalle> call() throws Exception {
                return planillaService.obtenerDetalles(planilla.getId());
            }
        };

        task.setOnSucceeded(e -> {
            List<PlanillaDetalle> todos = task.getValue();

            tablaIngresos.setItems(FXCollections.observableArrayList(
                    todos.stream().filter(d -> d.getMonto() > 0).collect(Collectors.toList())
            ));

            tablaDescuentos.setItems(FXCollections.observableArrayList(
                    todos.stream().filter(d -> d.getMonto() < 0).collect(Collectors.toList())
            ));

            double totalIng = todos.stream()
                    .filter(d -> d.getMonto() > 0)
                    .mapToDouble(PlanillaDetalle::getMonto)
                    .sum();

            double totalDesc = todos.stream()
                    .filter(d -> d.getMonto() < 0)
                    .mapToDouble(d -> Math.abs(d.getMonto()))
                    .sum();
            double neto = totalIng - totalDesc;

            lblTotalIngresos.setText(String.format("S/ %.2f", totalIng));
            lblTotalDescuentos.setText(String.format("S/ %.2f", totalDesc));
            lblNeto.setText(String.format("S/ %.2f", neto));
            lblBadgePlanilla.setText(
                    planilla.getNombreEmpleado() + "  —  " + planilla.getPeriodo());
        });

        task.setOnFailed(e -> {
            NotificacionService.error("Error al cargar detalle: " + task.getException().getMessage());
        });

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void limpiarDetalle() {
        tablaIngresos.setItems(FXCollections.emptyObservableList());
        tablaDescuentos.setItems(FXCollections.emptyObservableList());
        lblTotalIngresos.setText("S/ 0.00");
        lblTotalDescuentos.setText("S/ 0.00");
        lblNeto.setText("S/ 0.00");
        lblBadgePlanilla.setText("");
    }

    // ── Acciones ──────────────────────────────────────────────

    @FXML
    private void abrirGenerarPlanilla() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/project/fxml/pages/planilla/planillaGenerar.fxml"));
            Parent root = loader.load();
            Stage dialog = new Stage();
            dialog.setTitle("Generar Planilla");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root, 1100, 700));
            dialog.setOnHidden(e -> cargarPlanillas());
            dialog.show();
        } catch (Exception e) {
            NotificacionService.error("Error al abrir formulario: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarPlanillaSeleccionada() {
        if (isLoading) return;

        Planilla sel = tablaPlanillas.getSelectionModel().getSelectedItem();
        if (sel == null) {
            NotificacionService.advertencia("Selecciona una planilla para eliminar.");
            return;
        }
        if ("PAGADA".equals(sel.getEstado())) {
            NotificacionService.error("No se puede eliminar una planilla ya pagada.");
            return;
        }
        boolean ok = NotificacionService.confirmar(
                "¿Eliminar la planilla de " + sel.getNombreEmpleado()
                + " — periodo " + sel.getPeriodo() + "?");
        if (ok) {
            isLoading = true;
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    planillaService.eliminarPlanilla(sel.getId());
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                NotificacionService.exito("Planilla eliminada correctamente.");
                isLoading = false;
                cargarPlanillas();
            });

            task.setOnFailed(e -> {
                NotificacionService.error("Error al eliminar: " + task.getException().getMessage());
                isLoading = false;
            });

            Thread backgroundThread = new Thread(task);
            backgroundThread.setDaemon(true);
            backgroundThread.start();
        }
    }
    
    private void editarPlanilla(Planilla planilla) {
        if ("PAGADA".equals(planilla.getEstado())) {
            NotificacionService.error("No se puede editar una planilla ya pagada.");
            return;
        }

        // Crear el dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Planilla");
        dialog.setHeaderText("Editando planilla de: " + planilla.getNombreEmpleado());

        // Campos
        ComboBox<String> cbMes = new ComboBox<>();
        cbMes.getItems().addAll("01","02","03","04","05","06","07","08","09","10","11","12");

        ComboBox<String> cbAnio = new ComboBox<>();
        int anioActual = java.time.LocalDate.now().getYear();
        cbAnio.getItems().addAll(
            String.valueOf(anioActual - 1),
            String.valueOf(anioActual),
            String.valueOf(anioActual + 1)
        );

        String periodo = planilla.getPeriodo() != null ? planilla.getPeriodo().trim() : "";
        String[] partes = periodo.split("-");
        if (partes.length >= 2) {
            cbAnio.setValue(partes[0]);
            cbMes.setValue(partes[1]);
        } else {
            cbAnio.setValue(String.valueOf(java.time.LocalDate.now().getYear()));
            cbMes.setValue(String.format("%02d", java.time.LocalDate.now().getMonthValue()));
        }
        
        ComboBox<String> cbEstadoEdit = new ComboBox<>();
        cbEstadoEdit.getItems().addAll("GENERADA", "PAGADA", "ANULADA");
        cbEstadoEdit.setValue(planilla.getEstado()); 

        TextField txtMinutos = new TextField("0");
        TextField txtFaltas  = new TextField("0");
        
        
        

        // Layout del dialog
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        grid.add(new Label("Año:"),              0, 0);
        grid.add(cbAnio,                         1, 0);
        grid.add(new Label("Mes:"),              0, 1);
        grid.add(cbMes,                          1, 1);
        grid.add(new Label("Estado:"), 0, 2);
        grid.add(cbEstadoEdit,         1, 2);
        grid.add(new Label("Minutos tardanza:"), 0, 3);
        grid.add(txtMinutos,                     1, 3);
        grid.add(new Label("Días falta:"),       0, 4);
        grid.add(txtFaltas,                      1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    int minutos = Integer.parseInt(txtMinutos.getText().trim());
                    int faltas  = Integer.parseInt(txtFaltas.getText().trim());
                    String nuevoPeriodo = cbAnio.getValue() + "-" + cbMes.getValue();

                    // Recalcular detalles
                    List<PlanillaDetalle> nuevosDetalles = planillaService
                            .calcularDetallesPreview(planilla.getEmpleado_id(), minutos, faltas);

                    // Actualizar periodo y guardar
                    planilla.setPeriodo(nuevoPeriodo);
                    planilla.setEstado(cbEstadoEdit.getValue());
                    planillaService.actualizarPlanilla(planilla, nuevosDetalles, 1);

                    NotificacionService.exito("Planilla actualizada correctamente.");
                    cargarPlanillas();

                } catch (NumberFormatException e) {
                    NotificacionService.advertencia("Minutos y días deben ser números enteros.");
                } catch (Exception e) {
                    NotificacionService.error("Error al actualizar: " + e.getMessage());
                }
            }
        });
    }
    


    @FXML
    private void recargar() {
        cargarPlanillas();
    }
    
    
}