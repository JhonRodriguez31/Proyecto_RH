package com.project.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private DatabaseConfig() {
    }


    public static Connection getConnection() throws SQLException {

        String host = EnvConfig.get("DB_HOST", "localhost");
        String db = EnvConfig.get("DB_NAME");

        String encrypt = EnvConfig.get("DB_ENCRYPT", "true");
        String trustServerCertificate = EnvConfig.get("DB_TRUST_SERVER_CERTIFICATE", "true");

        
        String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=DBPlanilla;"
                + "user=sa;"
                + "password=11391491;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";

        return DriverManager.getConnection(url);
    }
}
