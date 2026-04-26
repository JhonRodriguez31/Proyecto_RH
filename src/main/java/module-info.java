module com.project.projectpoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;
    requires io.github.cdimascio.dotenv.java;
    requires java.net.http;
    requires com.google.gson;

    opens com.project to javafx.fxml;
    opens com.project.layout to javafx.fxml;
    opens com.project.controllers to javafx.fxml;
    opens com.project.models to javafx.fxml;
    opens com.project.services to javafx.fxml;
    opens com.project.services.impl to javafx.fxml;
    opens com.project.DAO to javafx.fxml;
    opens com.project.DAO.impl to javafx.fxml;
    opens com.project.config to javafx.fxml;
    opens com.project.common.enums to javafx.fxml;
    opens com.project.common.exception to javafx.fxml;
//    opens com.project.util to javafx.fxml;
    exports com.project;
}