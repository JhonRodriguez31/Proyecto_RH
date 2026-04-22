package com.project.layout;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AdminLayoutController extends BaseLayoutController {

    @FXML
    private VBox rootContainer;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnEmployees;
    @FXML
    private Button btnAttendance;
    @FXML
    private Button btnPayroll;
    @FXML
    private Button btnVacation;
    @FXML
    private Button btnBenefits;
    @FXML
    private Button btnAudit;

    @Override
    protected void setupMenu() {
        registerMenuButton(btnDashboard, "admin-dashboard", "/com/project/fxml/pages/admin/dashboard-view.fxml");
        registerMenuButton(btnEmployees, "admin-employees", "/com/project/fxml/pages/admin/employee-view.fxml");
        registerMenuButton(btnAttendance, "admin-attendance", "/com/project/fxml/pages/placeholder-view.fxml");
        registerMenuButton(btnPayroll, "admin-payroll", "/com/project/fxml/pages/placeholder-view.fxml");
        registerMenuButton(btnVacation, "admin-vacation", "/com/project/fxml/pages/admin/vacation-view.fxml");
        registerMenuButton(btnBenefits, "admin-benefits", "/com/project/fxml/pages/admin/benefit-view.fxml");
        registerMenuButton(btnAudit, "admin-audit", "/com/project/fxml/pages/placeholder-view.fxml");
    }

    @Override
    protected void goDefaultPage() {
        setActiveButton(btnDashboard);
        navigationService.navigate("admin-dashboard", "/com/project/fxml/pages/admin/dashboard-view.fxml");
    }

    @Override
    protected VBox getRootContainer() {
        return rootContainer;
    }
}