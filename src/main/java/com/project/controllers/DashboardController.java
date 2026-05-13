package com.project.controllers;

import com.project.config.ServiceFactory;
import com.project.services.DashboardService;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.Map;

public class DashboardController {

    @FXML private FlowPane tilesContainer;
    @FXML private PieChart areaChart;
    @FXML private BarChart<String, Number> attendanceChart;

    private final DashboardService dashboardService;

    public DashboardController() {
        this.dashboardService = ServiceFactory.getDashboardService();
    }

    private javafx.animation.Timeline refreshTimeline;

    @FXML
    public void initialize() {
        loadStatistics();
        setupAutoRefresh();
    }

    private void setupAutoRefresh() {
        refreshTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(5), e -> loadStatistics())
        );
        refreshTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void loadStatistics() {
        int totalEmployees = dashboardService.getTotalEmployees();
        int activeContracts = dashboardService.getActiveContractsCount();
        double monthlyPayroll = dashboardService.getMonthlyPayrollTotal();

        VBox card1 = createStatCard("Total Empleados", String.valueOf(totalEmployees), "Colaboradores", "#6c5ce7", "#a29bfe");
        VBox card2 = createStatCard("Contratos Activos", String.valueOf(activeContracts), "Vigentes", "#00b894", "#55efc4");
        VBox card3 = createStatCard("Costo Planilla", "S/ " + String.format("%,.0f", monthlyPayroll), "Mensual Estimado", "#e17055", "#fab1a0");

        tilesContainer.getChildren().clear();
        tilesContainer.getChildren().addAll(card1, card2, card3);

        // Pie Chart
        Map<String, Integer> areaData = dashboardService.getEmployeesByArea();
        javafx.collections.ObservableList<PieChart.Data> pieData = javafx.collections.FXCollections.observableArrayList();
        if (areaData.isEmpty()) {
            areaChart.setTitle("Sin datos de áreas");
        } else {
            areaChart.setTitle("");
            areaData.forEach((area, count) -> pieData.add(new PieChart.Data(area, count)));
        }
        areaChart.setData(pieData);

        // Bar Chart
        Map<String, Integer> attendanceData = dashboardService.getAttendanceStats();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Estado de Asistencia");
        if (attendanceData.isEmpty()) {
            series.getData().add(new XYChart.Data<>("Sin Datos", 0));
        } else {
            attendanceData.forEach((status, count) -> series.getData().add(new XYChart.Data<>(status, count)));
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
}
