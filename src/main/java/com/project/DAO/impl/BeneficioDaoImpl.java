package com.project.DAO.impl;

import com.project.DAO.BeneficioDAO;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.Beneficio;
import com.project.models.Salud;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeneficioDaoImpl implements BeneficioDAO {

    @Override
    public List<Beneficio> obtenerBeneficios() {
        List<Beneficio> beneficios = new ArrayList<>();
        String query = "SELECT * FROM Beneficio";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                beneficios.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener beneficios", e);
        }
        return beneficios;
    }

    @Override
    public boolean insertarBeneficio(Beneficio beneficio) {
        String query = "INSERT INTO Beneficio (nombre, monto, descripcion) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, beneficio.getNombre());
            stmt.setDouble(2, beneficio.getMonto());
            stmt.setString(3, beneficio.getDescripcion());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BaseDatosException("Error al insertar", e);
        }
    }

    @Override
    public boolean actualizarBeneficio(Beneficio beneficio) {
        String query = "UPDATE Beneficio SET nombre = ?, monto = ?, descripcion = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, beneficio.getNombre());
            stmt.setDouble(2, beneficio.getMonto());
            stmt.setString(3, beneficio.getDescripcion());
            stmt.setInt(4, beneficio.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BaseDatosException("Error al actualizar", e);
        }
    }

    @Override
    public boolean eliminarBeneficio(int id) {
        String sql = "DELETE FROM Beneficio WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BaseDatosException("Error al eliminar", e);
        }
    }

    private Beneficio mapearResultSet(ResultSet rs) throws SQLException {
        // Usamos Salud como base para que no sea null
        Beneficio b = new Salud();
        b.setId(rs.getInt("id"));
        b.setNombre(rs.getString("nombre"));
        b.setMonto(rs.getDouble("monto"));
        b.setDescripcion(rs.getString("descripcion"));
        return b;
    }
}