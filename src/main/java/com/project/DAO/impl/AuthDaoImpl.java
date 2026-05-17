package com.project.DAO.impl;

import com.project.DAO.AuthDao;
import com.project.common.enums.Role;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDaoImpl implements AuthDao {
    @Override
    public Usuario encontrarPorDni(String dni) {
        String query = "SELECT u.* FROM Usuario u " +
                "JOIN Empleado e ON u.empleado_id = e.id " +
                "WHERE e.dni = ?";

        try (Connection connection = DatabaseConfig.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dni);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(resultSet.getInt("id"));
                    usuario.setEmpleadoId(resultSet.getInt("empleado_id"));
                    usuario.setUserName(resultSet.getString("username"));
                    usuario.setEmail(resultSet.getString("email"));
                    usuario.setPassword(resultSet.getString("password"));
                    usuario.setRole(Role.valueOf(resultSet.getString("rol")));
                    usuario.setActivo(resultSet.getBoolean("activo"));
                    usuario.setPrimeraVez(resultSet.getBoolean("primera_vez"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener usuario por DNI", e);
        }

        return null;
    }

    @Override
    public void actualizarPassword(Integer usuarioId, String hashedPassword, boolean primeraVez) {
        String query = "UPDATE dbo.Usuario SET password = ?, primera_vez = ?, fecha_actualizacion = SYSDATETIME() WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, hashedPassword);
            stmt.setBoolean(2, primeraVez);
            stmt.setInt(3, usuarioId);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new BaseDatosException("No se pudo actualizar la contraseña", null);
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al actualizar la contraseña del usuario ID: " + usuarioId, e);
        }
    }
}
