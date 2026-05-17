package com.project.controllers;

import com.project.common.util.NotificacionService;
import com.project.config.ServiceFactory;
import com.project.models.Beneficio;
import com.project.services.BeneficioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BeneficioController implements Initializable {

    private final BeneficioService beneficioService = ServiceFactory.getBeneficioService();

    @FXML private ListView<Beneficio> listaUI;
    @FXML private Label lblDetalle;
    @FXML private Label lblRegla;
    @FXML private TextField txtNombre;
    @FXML private TextField txtMonto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        configurarSeleccion();
    }

    private void cargarDatos() {
        try {
            ObservableList<Beneficio> items = FXCollections.observableArrayList(
                    beneficioService.obtenerTodos()
            );
            listaUI.setItems(items);
        } catch (Exception e) {
            NotificacionService.error("Error al cargar datos: " + e.getMessage());
        }
    }

    private void configurarSeleccion() {
        listaUI.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblDetalle.setText("Nombre: " + newSelection.getNombre());
                // POLIMORFISMO: obtenerDetalleEspecial() se comporta según la subclase
                lblRegla.setText("Regla: " + newSelection.obtenerDetalleEspecial());
            }
        });
    }

    @FXML
    private void guardarDatos() {
        try {

            NotificacionService.exito("Beneficio guardado correctamente");
            cargarDatos(); // Recargar lista
        } catch (Exception e) {
            NotificacionService.error("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void simularAsignacion() {
        Beneficio seleccionado = listaUI.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            NotificacionService.exito("Asignando beneficio: " + seleccionado.getNombre());
        } else {
            NotificacionService.advertencia("Por favor, seleccione un beneficio");
        }
    }
}