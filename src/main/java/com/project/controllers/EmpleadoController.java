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
    private TableColumn<Empleado, String> colFoto;
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
    private TableColumn<Empleado, Void> colAcciones;

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

        colFoto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFotoUrl()));
        colFoto.setCellFactory(col -> new javafx.scene.control.TableCell<Empleado, String>() {
            private final javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
            {
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(15, 15, 15);
                imageView.setClip(clip);
            }

            @Override
            protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);
                if (empty || url == null || url.trim().isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        javafx.scene.image.Image image = new javafx.scene.image.Image(url, true);
                        imageView.setImage(image);
                        setGraphic(imageView);
                        setAlignment(javafx.geometry.Pos.CENTER);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        colAcciones.setCellFactory(param -> new javafx.scene.control.TableCell<Empleado, Void>() {
            private final Button btnVisualizar = new Button();
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(8, btnVisualizar, btnEditar, btnEliminar);

            {
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                org.kordamp.ikonli.javafx.FontIcon viewIcon = new org.kordamp.ikonli.javafx.FontIcon("mdi2e-eye");
                viewIcon.setIconSize(16);
                btnVisualizar.setGraphic(viewIcon);
                btnVisualizar.getStyleClass().addAll("btn-secondary");
                btnVisualizar.setStyle("-fx-padding: 5; -fx-background-radius: 5; -fx-cursor: hand;");
                btnVisualizar.setOnAction(e -> {
                    Empleado emp = getTableView().getItems().get(getIndex());
                    NotificacionService.info("Visualizar empleado: " + emp.getNombres());
                });

                org.kordamp.ikonli.javafx.FontIcon editIcon = new org.kordamp.ikonli.javafx.FontIcon("mdi2p-pencil");
                editIcon.setIconSize(16);
                btnEditar.setGraphic(editIcon);
                btnEditar.getStyleClass().addAll("btn-primary");
                btnEditar.setStyle("-fx-padding: 5; -fx-background-radius: 5; -fx-cursor: hand;");
                btnEditar.setOnAction(e -> {
                    Empleado emp = getTableView().getItems().get(getIndex());
                    NotificacionService.info("Editar empleado: " + emp.getNombres());
                });

                org.kordamp.ikonli.javafx.FontIcon deleteIcon = new org.kordamp.ikonli.javafx.FontIcon("mdi2t-trash-can");
                deleteIcon.setIconSize(16);
                btnEliminar.setGraphic(deleteIcon);
                btnEliminar.getStyleClass().addAll("btn-danger");
                btnEliminar.setStyle("-fx-padding: 5; -fx-background-radius: 5; -fx-cursor: hand;");
                btnEliminar.setOnAction(e -> {
                    Empleado emp = getTableView().getItems().get(getIndex());
                    eliminarEmpleado(emp);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
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

    private void eliminarEmpleado(Empleado seleccionado) {
        if (seleccionado == null) return;

        Alert confirma = new Alert(Alert.AlertType.CONFIRMATION);
        confirma.setTitle("Confirmar eliminación");
        confirma.setHeaderText("¿Eliminar a " + seleccionado.getNombres() + "?");
        confirma.setContentText("Esta acción no se puede deshacer");

        if (confirma.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                empleadoService.eliminarEmpleado(seleccionado.getId());
                NotificacionService.exito("Empleado eliminado");
                cargarEmpleados();
            } catch (Exception e) {
                NotificacionService.error("Error al eliminar: " + e.getMessage());
            }
        }
    }
}
