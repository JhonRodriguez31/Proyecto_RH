package com.project.controllers;

import com.project.common.util.NotificacionService;
import com.project.common.util.SessionManager;
import com.project.config.ServiceFactory;
import com.project.models.Empleado;
import com.project.services.EmpleadoService;
import com.project.services.ImageService;
import com.project.services.ReportService;
import com.project.services.impl.ImageServiceImpl;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmpleadoController implements Initializable {
    private final EmpleadoService empleadoService = ServiceFactory.getEmpleadoService();
    private final ReportService reportService = ServiceFactory.getReportService();
    private final ImageService imageService = new ImageServiceImpl();

    @FXML private TableView<Empleado> empleadosTable;
    @FXML private TableColumn<Empleado, String> colFoto;
    @FXML private TableColumn<Empleado, String> colCodigo;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colApellidos;
    @FXML private TableColumn<Empleado, String> colDNI;
    @FXML private TableColumn<Empleado, String> colTelefono;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnRecargar;
    @FXML private Button btnReporteGeneral;
    @FXML private Button btnFichaPdf;
    @FXML private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        cargarEmpleados();
        searchField.textProperty().addListener((obs, old, nuevo) -> filtrarEmpleados(nuevo));

        btnRecargar.setOnAction(e -> cargarEmpleados());
        btnAgregar.setOnAction(e -> abrirDialogoNuevoEmpleado());
        btnEditar.setOnAction(e -> editarEmpleadoSeleccionado());
        btnEliminar.setOnAction(e -> eliminarEmpleadoSeleccionado());
        btnReporteGeneral.setOnAction(e -> generarReporteGeneral());
        btnFichaPdf.setOnAction(e -> generarFichaEmpleadoSeleccionado());
    }

    private void configurarColumnas() {
        colFoto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFotoUrl()));
        colFoto.setCellFactory(column -> new TableCell<Empleado, String>() {
            private final ImageView imageView = new ImageView();
            private final Circle clip = new Circle(18, 18, 18);
            private final StackPane container = new StackPane();
            private final Label initialLabel = new Label();

            {
                imageView.setFitWidth(36);
                imageView.setFitHeight(36);
                imageView.setClip(clip);
                
                initialLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
                
                container.setAlignment(Pos.CENTER);
                container.setPrefSize(36, 36);
                container.setMaxSize(36, 36);
            }

            @Override
            protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Empleado emp = getTableRow().getItem();
                    container.getChildren().clear();
                    
                    String effectiveUrl = (url != null && !url.trim().isEmpty()) ? url : Empleado.DEFAULT_PHOTO_URL;
                    
                    try {
                        Image img = new Image(effectiveUrl, true);
                        imageView.setImage(img);
                        container.getChildren().add(imageView);
                        
                        // Si es la imagen por defecto, mostramos la inicial encima para que se vea mejor
                        if (effectiveUrl.equals(Empleado.DEFAULT_PHOTO_URL)) {
                            String initial = (emp.getNombres() != null && !emp.getNombres().isEmpty()) 
                                ? emp.getNombres().substring(0, 1).toUpperCase() : "?";
                            initialLabel.setText(initial);
                            container.getChildren().add(initialLabel);
                        }
                    } catch (Exception e) {
                        // Fallback total
                        Circle bg = new Circle(18, Color.web("#6366f1"));
                        String initial = (emp.getNombres() != null && !emp.getNombres().isEmpty()) 
                            ? emp.getNombres().substring(0, 1).toUpperCase() : "?";
                        initialLabel.setText(initial);
                        container.getChildren().addAll(bg, initialLabel);
                    }
                    
                    setGraphic(container);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigoEmpleado()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombres()));
        colApellidos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellidos()));
        colDNI.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDni()));
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
    }

    private void cargarEmpleados() {
        try {
            List<Empleado> empleados = empleadoService.obtenerEmpleados();
            empleadosTable.setItems(FXCollections.observableArrayList(empleados));
        } catch (Exception e) {
            e.printStackTrace();
            NotificacionService.error("Error al cargar empleados: " + e.getMessage());
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
        abrirDialogoFormulario(null);
    }

    @FXML
    private void editarEmpleadoSeleccionado() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            NotificacionService.advertencia("Selecciona un empleado para editar");
            return;
        }
        abrirDialogoFormulario(seleccionado);
    }

    private void abrirDialogoFormulario(Empleado empleado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/fxml/pages/admin/employee-form.fxml"));
            VBox root = loader.load();
            
            EmpleadoFormController controller = loader.getController();
            controller.setEmpleado(empleado);
            
            Stage stage = new Stage();
            stage.setTitle(empleado == null ? "Nuevo Empleado" : "Editar Empleado");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(empleadosTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            if (controller.isGuardado()) {
                Empleado empResultado = controller.getEmpleado();
                File fotoFile = controller.getArchivoFotoSeleccionado();

                if (fotoFile != null) {
                    try {
                        String url = imageService.subirImagen(fotoFile);
                        empResultado.setFotoUrl(url);
                    } catch (Exception e) {
                        NotificacionService.error("Error al subir la imagen: " + e.getMessage());
                        // Opcional: preguntar si desea continuar sin la imagen
                    }
                }

                if (empleado == null) {
                    empleadoService.registrarEmpleado(empResultado, SessionManager.getUsuarioId());
                    NotificacionService.exito("Empleado registrado correctamente");
                } else {
                    empleadoService.actualizarEmpleado(empResultado, SessionManager.getUsuarioId());
                    NotificacionService.exito("Empleado actualizado correctamente");
                }
                cargarEmpleados();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            NotificacionService.error("Error al abrir el formulario: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            NotificacionService.error("Error al guardar: " + e.getMessage());
        }
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
            } catch (Exception e) {
                NotificacionService.error("Error al eliminar: " + e.getMessage());
            }
        }
    }

    @FXML
    private void generarReporteGeneral() {
        try {
            List<Empleado> empleados = empleadosTable.getItems();
            if (empleados.isEmpty()) {
                NotificacionService.advertencia("No hay empleados para generar el reporte");
                return;
            }
            reportService.generarReporteEmpleados(empleados);
        } catch (Exception e) {
            e.printStackTrace();
            NotificacionService.error("Error al generar reporte: " + e.getMessage());
        }
    }

    @FXML
    private void generarFichaEmpleadoSeleccionado() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            NotificacionService.advertencia("Selecciona un empleado para generar su ficha");
            return;
        }

        try {
            reportService.generarFichaEmpleado(seleccionado);
        } catch (Exception e) {
            e.printStackTrace();
            NotificacionService.error("Error al generar ficha: " + e.getMessage());
        }
    }
}