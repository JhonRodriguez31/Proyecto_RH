package com.project.DAO.impl;

import com.project.DAO.PlanillaDAO;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.Planilla;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanillaDAOImpl implements PlanillaDAO {

    @Override
    public List<Planilla> listar() {
        List<Planilla> lista = new ArrayList<>();
        String sql = "EXEC sp_PlanillaListar";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al listar planillas", e);
        }
        return lista;
    }

    @Override
    public Planilla obtenerPorId(Integer id) {
        String sql = "EXEC sp_PlanillaObtener ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener planilla", e);
        }
        return null;
    }

    @Override
    public Integer registrar(Planilla p) {
        String sql = "EXEC sp_PlanillaRegistrar ?, ?, ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, p.getEmpleado_id());
        	stmt.setString(2, p.getPeriodo());
        	stmt.setString(3, p.getEstado());

        	ResultSet rs = stmt.executeQuery();

        	if (rs.next()) {
        	    return rs.getInt(1); 
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al registrar planilla", e);
        }
        return null;
    }

    @Override
    public void actualizar(Planilla p) {
        String sql = "EXEC sp_PlanillaActualizarEstado ?, ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getEstado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al actualizar planilla", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "EXEC sp_PlanillaEliminar ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al eliminar planilla", e);
        }
    }

    // ── Mapeo ─────────────────────────────────────────

    private Planilla mapear(ResultSet rs) throws SQLException {
        Planilla p = new Planilla();
        p.setId(rs.getInt("id"));
        p.setEmpleado_id(rs.getInt("empleado_id"));
        p.setContrato_id(rs.getInt("contrato_id"));
        p.setPeriodo(rs.getString("periodo"));
        p.setFecha_generacion(rs.getString("fecha_generacion"));
        p.setEstado(rs.getString("estado"));

        // Campos extra para la vista (no son del modelo base, los guardamos como strings)
        try { p.setNombreEmpleado(rs.getString("nombre_empleado")); } catch (SQLException ignored) {}
        try { p.setSueldoBase(rs.getBigDecimal("sueldo_base")); } catch (SQLException ignored) {}
        try { p.setCargo(rs.getString("cargo")); } catch (SQLException ignored) {}

        return p;
    }
}
