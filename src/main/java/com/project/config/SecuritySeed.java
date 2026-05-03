package com.project.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SecuritySeed {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String getEmpleadoIdSQL = "SELECT id FROM empleado WHERE dni = ?";
            String insertUserSQL = "INSERT INTO usuario (username, password, rol, activo, empleado_id,email) VALUES (?, ?, ?, ?, ?,?)";

            int empleadoUserId = getEmpleadoId(connection, getEmpleadoIdSQL, "71234567");
            createUser(connection, insertUserSQL, "test_user", "user123", "USER", empleadoUserId, "testuser@gmail.com");

            int empleadoAdminId = getEmpleadoId(connection, getEmpleadoIdSQL, "72345678");
            createUser(connection, insertUserSQL, "test_admin", "admin123", "ADMIN", empleadoAdminId, "testadmin@gmail.com");

            System.out.println("Usuarios de prueba creados correctamente.");
        } catch (
                Exception e) {
            e.printStackTrace();
            System.err.println("Error al crear los usuarios de prueba.");
        }
    }

    private static int getEmpleadoId(Connection connection, String getEmpleadoIdSQL, String dni) throws Exception {
        try (PreparedStatement getEmpleadoStmt = connection.prepareStatement(getEmpleadoIdSQL)) {
            getEmpleadoStmt.setString(1, dni);
            try (ResultSet resultSet = getEmpleadoStmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new Exception("No se encontró un empleado con el DNI: " + dni);
                }
            }
        }
    }

    private static void createUser(Connection connection, String insertUserSQL, String username, String password, String role, int empleadoId, String email) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(insertUserSQL)) {
            statement.setString(1, username);
            statement.setString(2, BcryptFactory.hashPassword(password));
            statement.setString(3, role);
            statement.setBoolean(4, true);
            statement.setInt(5, empleadoId);
            statement.setString(6, email);
            statement.executeUpdate();
        }
    }
}