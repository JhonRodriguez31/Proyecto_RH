package com.project.layout;

import com.project.common.util.NavigationService;
import com.project.config.ServiceFactory;
import com.project.services.AuthService;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class BaseLayoutController {
    @FXML
    protected StackPane contentHost;
    @FXML
    protected Label dateLabel;
    @FXML
    protected Button themeButton;
    @FXML
    protected VBox sidebar;
    @FXML
    protected Button menuButton;
    @FXML
    protected Label lblUsuarioNombre;
    @FXML
    protected Label lblUsuarioRol;
    @FXML
    protected Label lblAvatarInitial;
    @FXML
    protected javafx.scene.image.ImageView imgAvatar;


    protected NavigationService navigationService;
    protected final List<Button> menuButtons = new ArrayList<>();
    protected boolean darkMode = true;
    protected boolean sidebarCollapsed = false;

    @FXML
    public void initialize() {
        navigationService = new NavigationService(contentHost);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-PE"));
        dateLabel.setText(LocalDate.now().format(formatter));
        setupMenu();
        goDefaultPage();
        cargarDatosUsuario();
    }

    protected void cargarDatosUsuario() {
        com.project.services.AuthService authService = com.project.config.ServiceFactory.getAuthService();
        if (authService == null)
            return;

        com.project.models.Usuario usuario = authService.obtenerUsuarioAutenticado();
        if (usuario != null) {
            com.project.services.EmpleadoService empleadoService = com.project.config.ServiceFactory.getEmpleadoService();
            if (empleadoService == null)
                return;

            com.project.models.Empleado empleado = null;
            if (usuario.getEmpleadoId() != null) {
                empleado = empleadoService.obtenerEmpleado(usuario.getEmpleadoId());
            }

            if (empleado != null) {
                if (lblUsuarioNombre != null) {
                    lblUsuarioNombre.setText(empleado.getNombres());
                }
                if (lblUsuarioRol != null) {
                    lblUsuarioRol.setText(usuario.getRole().toString());
                }
                if (lblAvatarInitial != null && !empleado.getNombres().isEmpty()) {
                    lblAvatarInitial.setText(empleado.getNombres().substring(0, 1).toUpperCase());
                }
                if (imgAvatar != null && empleado.getFotoUrl() != null && !empleado.getFotoUrl().trim().isEmpty()) {
                    try {
                        javafx.scene.image.Image image = new javafx.scene.image.Image(empleado.getFotoUrl(), true);
                        imgAvatar.setImage(image);
                        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(18, 18, 18);
                        imgAvatar.setClip(clip);
                        if (lblAvatarInitial != null) {
                            lblAvatarInitial.setVisible(false);
                        }
                    } catch (
                            Exception e) {
                        if (lblAvatarInitial != null) {
                            lblAvatarInitial.setVisible(true);
                        }
                    }
                }
            } else {
                if (lblUsuarioNombre != null) {
                    lblUsuarioNombre.setText(usuario.getUserName());
                }
                if (lblUsuarioRol != null) {
                    lblUsuarioRol.setText(usuario.getRole().toString());
                }
                if (lblAvatarInitial != null && usuario.getUserName() != null && !usuario.getUserName().isEmpty()) {
                    lblAvatarInitial.setText(usuario.getUserName().substring(0, 1).toUpperCase());
                }
            }
        }
    }

    protected abstract void setupMenu();

    protected abstract void goDefaultPage();

    protected void registerMenuButton(Button button, String key, String fxmlPath) {
        menuButtons.add(button);
        button.setOnAction(e -> {
            setActiveButton(button);
            navigationService.navigate(key, fxmlPath);
        });
    }

    protected void setActiveButton(Button active) {
        for (Button b : menuButtons) {
            b.getStyleClass().remove("menu-item-active");
        }
        if (!active.getStyleClass().contains("menu-item-active")) {
            active.getStyleClass().add("menu-item-active");
        }
    }

    @FXML
    protected void toggleTheme() {
        darkMode = !darkMode;
        VBox root = getRootContainer();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.85);
        fadeOut.setInterpolator(Interpolator.EASE_BOTH);

        fadeOut.setOnFinished(e -> {
            if (darkMode) {
                root.getStyleClass().remove("theme-light");
            } else {
                if (!root.getStyleClass().contains("theme-light")) {
                    root.getStyleClass().add("theme-light");
                }
            }
            // Switch icon between moon and sun
            FontIcon icon = (FontIcon) themeButton.getGraphic();
            icon.setIconLiteral(darkMode ? "mdi2w-weather-night" : "mdi2w-weather-sunny");

            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), root);
            fadeIn.setFromValue(0.85);
            fadeIn.setToValue(1.0);
            fadeIn.setInterpolator(Interpolator.EASE_BOTH);
            fadeIn.play();
        });

        fadeOut.play();
    }

    @FXML
    protected void toggleSidebar() {
        if (sidebar == null) {
            return;
        }

        sidebarCollapsed = !sidebarCollapsed;
        double targetWidth = sidebarCollapsed ? 72 : 260;

        if (sidebarCollapsed) {
            updateSidebarForCollapse(false);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(280),
                        new KeyValue(sidebar.prefWidthProperty(), targetWidth, Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0)),
                        new KeyValue(sidebar.minWidthProperty(), targetWidth, Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0)),
                        new KeyValue(sidebar.maxWidthProperty(), targetWidth, Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0))
                )
        );

        if (!sidebarCollapsed) {
            timeline.setOnFinished(e -> updateSidebarForCollapse(true));
        }

        timeline.play();

        sidebar.getStyleClass().remove("sidebar-collapsed");
        if (sidebarCollapsed) {
            sidebar.getStyleClass().add("sidebar-collapsed");
        }
    }

    private void updateSidebarForCollapse(boolean expanded) {
        boolean passedFirstMenuItem = false;

        for (Node child : sidebar.getChildren()) {
            if (child instanceof Button && child.getStyleClass().contains("menu-item")) {
                passedFirstMenuItem = true;
                continue;
            }

            if (!passedFirstMenuItem) {
                child.setVisible(expanded);
                child.setManaged(expanded);
                continue;
            }

            if (child instanceof Region && VBox.getVgrow(child) == Priority.ALWAYS) {
                continue;
            }

            if (child.getStyleClass().contains("sidebar-divider")) {
                continue;
            }

            if (child.getStyleClass().contains("user-card") && child instanceof HBox hbox) {
                hbox.setAlignment(expanded ? Pos.CENTER_LEFT : Pos.CENTER);
                for (Node cardChild : hbox.getChildren()) {
                    if (cardChild instanceof VBox) {
                        cardChild.setVisible(expanded);
                        cardChild.setManaged(expanded);
                    }
                }
                continue;
            }

            if (child instanceof Button btn && child.getStyleClass().contains("logout-btn")) {
                btn.setContentDisplay(expanded ? ContentDisplay.LEFT : ContentDisplay.GRAPHIC_ONLY);
                continue;
            }

        }

        for (Button btn : menuButtons) {
            Node graphic = btn.getGraphic();
            if (graphic instanceof HBox hbox) {
                for (Node gChild : hbox.getChildren()) {
                    if (gChild.getStyleClass().contains("menu-text")) {
                        gChild.setVisible(expanded);
                        gChild.setManaged(expanded);
                    }
                }
            }
        }
    }

    protected abstract VBox getRootContainer();


    @FXML
    protected void handleLogout() {
        try {
            AuthService authService = ServiceFactory.getAuthService();
            if (authService != null) {
                authService.logout();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/fxml/auth/login.fxml"));

            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, 1200, 700);

            URL cssUrl = getClass().getResource("/com/project/css/auth-layout.css");

            if (cssUrl != null) {
                loginScene.getStylesheets().add(cssUrl.toExternalForm());
            }
            Stage stage = (Stage) getRootContainer().getScene().getWindow();

            stage.setScene(loginScene);
            stage.setTitle("Sistema de Recursos Humanos - Iniciar Sesion");
            stage.centerOnScreen();
        } catch (
                IOException e) {
            e.printStackTrace();
            System.out.println("Errror al regresar a la pantalla de login");
        }
    }
}
