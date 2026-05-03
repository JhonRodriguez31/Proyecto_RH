package com.project.controllers;


import com.project.common.util.NotificacionService;
import com.project.config.ServiceFactory;
import com.project.models.Empleado;
import com.project.services.EmpleadoService;
import com.project.services.impl.EmpleadoServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmpleadoController implements Initializable {
    EmpleadoService empleadoService = ServiceFactory.getEmpleadoService();

    @FXML
    private TableView<Empleado> empleadosTable;
    @FXML
    private TableColumn<Empleado, String> colCodigo;

    @FXML
    private TableColumn<Empleado, String> colNombre;

    @FXML
    private TableColumn<Empleado, String> colApellidos;

    @FXML
    private TableColumn<Empleado, String> colDNI;

    @FXML
    private TableColumn<Empleado, String> colTelefono;
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnRecargar;

    // Search
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        cargarEmpleados();
        searchField.textProperty().addListener((obs, old, nuevo) -> {
            filtrarEmpleados(nuevo);
        });

        btnRecargar.setOnAction(e -> cargarEmpleados());
        btnAgregar.setOnAction(e -> abrirDialogoNuevoEmpleado());
        btnEditar.setOnAction(e -> editarEmpleadoSeleccionado());
        btnEliminar.setOnAction(e -> eliminarEmpleadoSeleccionado());

    }


    private void configurarColumnas() {
        colCodigo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCodigoEmpleado())
        );
        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombres())
        );
        colApellidos.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getApellidos())
        );
        colDNI.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDni())
        );
        colTelefono.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTelefono())
        );
    }


    private void cargarEmpleados() {
        try {
            List<Empleado> empleados = empleadoService.obtenerEmpleados();
            ObservableList<Empleado> observableList = FXCollections.observableArrayList(empleados);
            empleadosTable.setItems(observableList);
        } catch (
                Exception e) {
            e.printStackTrace();
            NotificacionService.error("Error al cargar empleados" + e.getMessage());
        }
    }

    private void filtrarEmpleados(String filtro) {
        List<Empleado> empleados = empleadoService.obtenerEmpleados();
        ObservableList<Empleado> filtrados = FXCollections.observableArrayList();

        if (filtro == null || filtro.isEmpty()) {
            filtrados.addAll(empleados);
        } else {
            String filtroLower = filtro.toLowerCase();
            for (Empleado emp : empleados) {
                if (emp.getNombres().toLowerCase().contains(filtroLower) ||
                        emp.getApellidos().toLowerCase().contains(filtroLower) ||
                        emp.getDni().toLowerCase().contains(filtroLower)) {
                    filtrados.add(emp);
                }
            }
        }

        empleadosTable.setItems(filtrados);
    }

    @FXML
    private void abrirDialogoNuevoEmpleado() {
        NotificacionService.info("Agregar nuevo empleado (por implementar)");
    }

    @FXML
    private void editarEmpleadoSeleccionado() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            NotificacionService.advertencia("Selecciona un empleado para editar");
            return;
        }
        NotificacionService.info("Editar empleado: " + seleccionado.getNombres());
    }

    @FXML
    private void eliminarEmpleadoSeleccionado() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            NotificacionService.advertencia("Selecciona un empleado para eliminar");
            return;
        }

        Alert confirma = new Alert(Alert.AlertType.CONFIRMATION);
        confirma.setTitle("Confirmar eliminación");
        confirma.setHeaderText("¿Eliminar a " + seleccionado.getNombres() + "?");
        confirma.setContentText("Esta acción no se puede deshacer");

        if (confirma.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                empleadoService.eliminarEmpleado(seleccionado.getId());
                NotificacionService.exito("Empleado eliminado");
                cargarEmpleados();
            } catch (
                    Exception e) {
                NotificacionService.error("Error al eliminar: " + e.getMessage());
            }
        }
    }
}
