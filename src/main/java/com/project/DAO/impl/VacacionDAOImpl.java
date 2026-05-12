package com.project.DAO.impl;

import com.project.DAO.VacacionDAO;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.Vacacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacacionDAOImpl implements VacacionDAO {

    @Override
    public List<Vacacion> listar() {
        List<Vacacion> lista = new ArrayList<>();
        String sql = "EXEC sp_VacacionListar";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al listar vacaciones", e);
        }
        return lista;
    }

    @Override
    public List<Vacacion> listarPorEmpleado(Integer empleadoId) {
        List<Vacacion> lista = new ArrayList<>();
        String sql = "EXEC sp_VacacionListarPorEmpleado ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al listar vacaciones del empleado", e);
        }
        return lista;
    }

    @Override
    public Integer registrar(Vacacion v) {
        String sql = "EXEC sp_VacacionRegistrar ?, ?, ?, ?, ?, ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getEmpleadoId());
            stmt.setDate(2, Date.valueOf(v.getFechaSolicitud()));
            stmt.setDate(3, Date.valueOf(v.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(v.getFechaFin()));
            stmt.setInt(5, v.getDiasSolicitados());
            stmt.setString(6, v.getObservacion());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("nuevo_id");
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al registrar vacación", e);
        }
        return null;
    }

    @Override
    public void aprobar(Integer vacacionId, Integer revisadoPor) {
        String sql = "EXEC sp_VacacionAprobar ?, ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vacacionId);
            stmt.setInt(2, revisadoPor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al aprobar vacación", e);
        }
    }

    @Override
    public void rechazar(Integer vacacionId, Integer revisadoPor, String observacion) {
        String sql = "EXEC sp_VacacionRechazar ?, ?, ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vacacionId);
            stmt.setInt(2, revisadoPor);
            stmt.setString(3, observacion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al rechazar vacación", e);
        }
    }

    // ── Mapeo ResultSet → Vacacion ────────────────────

    private Vacacion mapear(ResultSet rs) throws SQLException {
        Vacacion v = new Vacacion();
        v.setId(rs.getInt("id"));
        v.setEmpleadoId(rs.getInt("empleado_id"));
        v.setNombreEmpleado(rs.getString("nombre_empleado"));

        Date fSol = rs.getDate("fecha_solicitud");
        Date fIni = rs.getDate("fecha_inicio");
        Date fFin = rs.getDate("fecha_fin");

        if (fSol != null) v.setFechaSolicitud(fSol.toLocalDate());
        if (fIni != null) v.setFechaInicio(fIni.toLocalDate());
        if (fFin != null) v.setFechaFin(fFin.toLocalDate());

        v.setDiasSolicitados(rs.getInt("dias_solicitados"));
        v.setEstado(rs.getString("estado"));
        v.setObservacion(rs.getString("observacion"));

        int rev = rs.getInt("revisado_por");
        if (!rs.wasNull()) v.setRevisadoPor(rev);

        return v;
    }
}
