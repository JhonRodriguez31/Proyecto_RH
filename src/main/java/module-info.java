module com.project.projectpoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;
    requires io.github.cdimascio.dotenv.java;

    opens com.project to javafx.fxml;
    exports com.project;
}