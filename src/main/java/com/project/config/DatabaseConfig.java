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
        String port = EnvConfig.get("DB_PORT", "1433");
        String db = EnvConfig.get("DB_NAME");
        String user = EnvConfig.get("DB_USER");
        String password = EnvConfig.get("DB_PASSWORD");

        String encrypt = EnvConfig.get("DB_ENCRYPT", "true");
        String trustServerCertificate = EnvConfig.get("DB_TRUST_SERVER_CERTIFICATE", "true");
        String url = "jdbc:sqlserver://" + host + ":" + port + ";"
                + "databaseName=" + db + ";"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";";

        Properties props = new Properties();

        props.setProperty("user", user);
        props.setProperty("password", password);

        return DriverManager.getConnection(url, props);
    }

}
