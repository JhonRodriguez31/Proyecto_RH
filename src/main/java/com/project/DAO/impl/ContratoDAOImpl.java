package com.project.DAO.impl;

import com.project.DAO.ContratoDAO;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.Contrato;

import java.math.BigDecimal;
import java.sql.*;

public class ContratoDAOImpl implements ContratoDAO {

    @Override
    public Contrato obtenerActivoPorEmpleado(Integer empleadoId) {
        String sql = "EXEC sp_ContratoActivoPorEmpleado ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener contrato activo", e);
        }
        return null; 
    }

    private Contrato mapear(ResultSet rs) throws SQLException {
        Contrato c = new Contrato();
        c.setId(rs.getInt("id"));
        c.setEmpleadoId(rs.getInt("empleado_id"));
        c.setCargo(rs.getString("cargo"));
        c.setArea(rs.getString("area"));
        c.setTipoContrato(rs.getString("tipo_contrato"));

        Date fi = rs.getDate("fecha_inicio");
        Date ff = rs.getDate("fecha_fin");
        if (fi != null) c.setFechaInicio(fi.toLocalDate());
        if (ff != null) c.setFechaFin(ff.toLocalDate());

        BigDecimal sueldo = rs.getBigDecimal("sueldo_base");
        c.setSueldoBase(sueldo);
        c.setSistemaPension(rs.getString("sistema_pension"));
        c.setEstado(rs.getString("estado"));
        return c;
    }
}
