package com.project.layout;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class UserLayoutController extends BaseLayoutController {

    @FXML
    private VBox rootContainer;
    @FXML
    private Button btnPanel;
    @FXML
    private Button btnDirectory;
    @FXML
    private Button btnAttendance;
    @FXML
    private Button btnPayroll;
    @FXML
    private Button btnVacation;
    @FXML
    private Button btnBenefits;

    @Override
    protected void setupMenu() {
        registerMenuButton(btnPanel, "user-panel", "/com/project/fxml/pages/placeholder-view.fxml");
        registerMenuButton(btnDirectory, "user-directory", "/com/project/fxml/pages/placeholder-view.fxml");
        registerMenuButton(btnAttendance, "user-attendance", "/com/project/fxml/pages/placeholder-view.fxml");
        registerMenuButton(btnPayroll, "user-payroll", "/com/project/fxml/pages/placeholder-view.fxml");
        registerMenuButton(btnVacation, "user-vacation", "/com/project/fxml/pages/placeholder-view.fxml");
        registerMenuButton(btnBenefits, "user-benefits", "/com/project/fxml/pages/placeholder-view.fxml");
    }

    @Override
    protected void goDefaultPage() {
        setActiveButton(btnPanel);
        navigationService.navigate("user-panel", "/com/project/fxml/pages/placeholder-view.fxml");
    }

    @Override
    protected VBox getRootContainer() {
        return rootContainer;
    }
}