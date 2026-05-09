package com.project.DAO.impl;

import com.project.DAO.AsistenciaDao;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.Asistencia;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDaoImpl implements AsistenciaDao {

    @Override
    public void registrarEntrada(Asistencia asistencia) {
        String sql = "INSERT INTO Asistencia (empleado_id, fecha, hora_entrada, estado, minutos_tardanza, creado_por) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, asistencia.getEmpleadoId());
            stmt.setDate(2, Date.valueOf(asistencia.getFecha()));
            stmt.setTimestamp(3, Timestamp.valueOf(asistencia.getHoraEntrada()));
            stmt.setString(4, asistencia.getEstado());
            stmt.setInt(5, asistencia.getMinutosTardanza());
            stmt.setObject(6, asistencia.getCreadoPor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al registrar entrada", e);
        }
    }

    @Override
    public void registrarSalida(Integer empleadoId, LocalDate fecha) {
        String sql = "UPDATE Asistencia SET hora_salida = ? WHERE empleado_id = ? AND fecha = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, empleadoId);
            stmt.setDate(3, Date.valueOf(fecha));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al registrar salida", e);
        }
    }

    @Override
    public Asistencia obtenerPorEmpleadoYFecha(Integer empleadoId, LocalDate fecha) {
        String sql = "SELECT * FROM Asistencia WHERE empleado_id = ? AND fecha = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            stmt.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAsistencia(rs);
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener asistencia", e);
        }
        return null;
    }

    @Override
    public List<Asistencia> obtenerHistorialPorEmpleado(Integer empleadoId) {
        String sql = "SELECT * FROM Asistencia WHERE empleado_id = ? ORDER BY fecha DESC";
        List<Asistencia> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsistencia(rs));
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener historial", e);
        }
        return lista;
    }

    @Override
    public List<Asistencia> obtenerAsistenciasDeHoy() {
        String sql = "SELECT * FROM Asistencia WHERE fecha = ?";
        List<Asistencia> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsistencia(rs));
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener asistencias de hoy", e);
        }
        return lista;
    }

    @Override
    public void justificarAsistencia(Integer asistenciaId, String motivo, Integer adminId) {
        String sql = "UPDATE Asistencia SET motivo_justificacion = ?, estado = 'JUSTIFICADO', actualizado_por = ?, fecha_actualizacion = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, motivo);
            stmt.setInt(2, adminId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, asistenciaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al justificar asistencia", e);
        }
    }

    @Override
    public void corregirAsistencia(Asistencia asistencia, Integer adminId) {
        String sql = "UPDATE Asistencia SET hora_entrada = ?, hora_salida = ?, estado = ?, minutos_tardanza = ?, observacion = ?, corregido_por = ?, fecha_actualizacion = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, asistencia.getHoraEntrada() != null ? Timestamp.valueOf(asistencia.getHoraEntrada()) : null);
            stmt.setTimestamp(2, asistencia.getHoraSalida() != null ? Timestamp.valueOf(asistencia.getHoraSalida()) : null);
            stmt.setString(3, asistencia.getEstado());
            stmt.setInt(4, asistencia.getMinutosTardanza());
            stmt.setString(5, asistencia.getObservacion());
            stmt.setInt(6, adminId);
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(8, asistencia.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseDatosException("Error al corregir asistencia", e);
        }
    }

    private Asistencia mapearAsistencia(ResultSet rs) throws SQLException {
        Asistencia a = new Asistencia();
        a.setId(rs.getInt("id"));
        a.setEmpleadoId(rs.getInt("empleado_id"));
        a.setFecha(rs.getDate("fecha").toLocalDate());
        Timestamp entrada = rs.getTimestamp("hora_entrada");
        if (entrada != null) a.setHoraEntrada(entrada.toLocalDateTime());
        Timestamp salida = rs.getTimestamp("hora_salida");
        if (salida != null) a.setHoraSalida(salida.toLocalDateTime());
        a.setEstado(rs.getString("estado"));
        a.setMinutosTardanza(rs.getInt("minutos_tardanza"));
        a.setMotivoJustificacion(rs.getString("motivo_justificacion"));
        a.setCorregidoPor(rs.getObject("corregido_por", Integer.class));
        a.setObservacion(rs.getString("observacion"));
        return a;
    }
}