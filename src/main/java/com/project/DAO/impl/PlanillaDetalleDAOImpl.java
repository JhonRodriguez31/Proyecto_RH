package com.project.DAO.impl;

import com.project.DAO.PlanillaDetalleDAO;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.PlanillaDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanillaDetalleDAOImpl implements PlanillaDetalleDAO {

    @Override
    public List<PlanillaDetalle> listarPorPlanilla(Integer planillaId) {
        List<PlanillaDetalle> lista = new ArrayList<>();
        String sql = "EXEC sp_PlanillaDetalleListar ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, planillaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al listar detalle de planilla", e);
        }
        return lista;
    }

    @Override
    public void registrarDetalle(PlanillaDetalle d) {
        String sql = "EXEC sp_PlanillaDetalleInsertar ?, ?, ?, ?, ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getPlanilla_id());
            stmt.setInt(2, d.getConcepto_id());
            stmt.setDouble(3, d.getMonto());
            stmt.setDouble(4, d.getCantidad());
            stmt.setString(5, d.getDescripcion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al registrar detalle", e);
        }
    }

    @Override
    public void eliminarPorPlanilla(Integer planillaId) {
        String sql = "EXEC sp_PlanillaDetalleEliminar ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, planillaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al eliminar detalle de planilla", e);
        }
    }

    private PlanillaDetalle mapear(ResultSet rs) throws SQLException {
        PlanillaDetalle d = new PlanillaDetalle();
        d.setId(rs.getInt("id"));
        d.setPlanilla_id(rs.getInt("planilla_id"));
        d.setConcepto_id(rs.getInt("concepto_id"));
        d.setConceptoNombre(rs.getString("concepto_nombre"));
        d.setConceptoTipo(rs.getString("concepto_tipo"));
        d.setMonto(rs.getDouble("monto"));
        d.setCantidad(rs.getDouble("cantidad"));
        d.setDescripcion(rs.getString("descripcion"));
        return d;
    }
}
