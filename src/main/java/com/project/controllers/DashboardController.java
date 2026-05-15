package com.project.controllers;

import com.project.config.ServiceFactory;
import com.project.services.DashboardService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.Map;

public class DashboardController {

    @FXML private FlowPane tilesContainer;
    @FXML private PieChart areaChart;
    @FXML private BarChart<String, Number> attendanceChart;

    private final DashboardService dashboardService;
    private javafx.animation.Timeline refreshTimeline;
    private boolean isLoading = false;

    public DashboardController() {
        this.dashboardService = ServiceFactory.getDashboardService();
    }

    @FXML
    public void initialize() {
        loadStatisticsAsync();
        setupAutoRefresh();
    }

    private void setupAutoRefresh() {
        refreshTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(30), e -> loadStatisticsAsync())
        );
        refreshTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void loadStatisticsAsync() {
        if (isLoading) return; // Evita superponer peticiones
        isLoading = true;

        Task<DashboardData> task = new Task<>() {
            @Override
            protected DashboardData call() throws Exception {
                DashboardData data = new DashboardData();
                data.totalEmployees = dashboardService.getTotalEmployees();
                data.activeContracts = dashboardService.getActiveContractsCount();
                data.monthlyPayroll = dashboardService.getMonthlyPayrollTotal();
                data.areaData = dashboardService.getEmployeesByArea();
                data.attendanceData = dashboardService.getAttendanceStats();
                return data;
            }
        };

        task.setOnSucceeded(event -> {
            DashboardData data = task.getValue();
            updateUI(data);
            isLoading = false;
        });

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            System.err.println("Error al cargar dashboard: " + ex.getMessage());
            isLoading = false;
        });

        // Ejecutar en un hilo separado
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void updateUI(DashboardData data) {
        VBox card1 = createStatCard("Total Empleados", String.valueOf(data.totalEmployees), "Colaboradores", "#6c5ce7", "#a29bfe");
        VBox card2 = createStatCard("Contratos Activos", String.valueOf(data.activeContracts), "Vigentes", "#00b894", "#55efc4");
        VBox card3 = createStatCard("Costo Planilla", "S/ " + String.format("%,.0f", data.monthlyPayroll), "Mensual Estimado", "#e17055", "#fab1a0");

        tilesContainer.getChildren().setAll(card1, card2, card3);

        // Pie Chart
        javafx.collections.ObservableList<PieChart.Data> pieData = javafx.collections.FXCollections.observableArrayList();
        if (data.areaData.isEmpty()) {
            areaChart.setTitle("Sin datos de áreas");
        } else {
            areaChart.setTitle("");
            data.areaData.forEach((area, count) -> pieData.add(new PieChart.Data(area, count)));
        }
        areaChart.setData(pieData);

        // Bar Chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Estado de Asistencia");
        if (data.attendanceData.isEmpty()) {
            series.getData().add(new XYChart.Data<>("Sin Datos", 0));
        } else {
            data.attendanceData.forEach((status, count) -> series.getData().add(new XYChart.Data<>(status, count)));
        }
        attendanceChart.getData().clear();
        attendanceChart.getData().add(series);
    }

    private VBox createStatCard(String title, String value, String description, String colorDark, String colorLight) {
        VBox card = new VBox(8);
        card.setPrefSize(220, 130);
        card.setPadding(new Insets(18));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + colorDark + ", " + colorLight + ");" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        );

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.85); -fx-font-weight: bold;");

        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label lblDesc = new Label(description);
        lblDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.7);");

        card.getChildren().addAll(lblTitle, lblValue, lblDesc);
        return card;
    }

    // Clase auxiliar para transportar los datos del hilo de fondo al hilo de UI
    private static class DashboardData {
        int totalEmployees;
        int activeContracts;
        double monthlyPayroll;
        Map<String, Integer> areaData;
        Map<String, Integer> attendanceData;
    }

    public void stopAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
    }
}
