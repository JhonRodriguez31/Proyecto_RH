package com.project.DAO.impl;

import com.project.DAO.UserDao;
import com.project.common.enums.Role;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public List<Usuario> getAllUsers() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT id,email,password,role from users";
        try (Connection connection = DatabaseConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query);
        ) {
            while (rs.next()) {
                Usuario usuario = new Usuario();

                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRole(Role.valueOf(rs.getString("role")));
                usuarios.add(usuario);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        String query = "INSERT INTO dbo.Usuario (empleado_id, rol, username, email, password, primera_vez, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, usuario.getEmpleadoId());
            stmt.setString(2, usuario.getRole().name());
            stmt.setString(3, usuario.getUserName());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getPassword());
            stmt.setBoolean(6, usuario.getPrimeraVez());
            stmt.setBoolean(7, usuario.getActivo());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new RuntimeException("No se pudo crear el usuario");
            }
        } catch (
                SQLException e) {
            throw new BaseDatosException("Error al crear usuario : " + usuario.toString(), e);
        }

    }

    @Override
    public Usuario obtenerPorUsername(String userName) {
        return null;
    }
}
