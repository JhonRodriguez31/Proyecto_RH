package com.project.DAO.impl;

import com.project.DAO.PerfilDao;
import com.project.common.exception.BaseDatosException;
import com.project.config.DatabaseConfig;
import com.project.models.PerfilEmpleadoDTO;

import java.sql.*;

public class PerfilDaoImpl implements PerfilDao {

    @Override
    public PerfilEmpleadoDTO obtenerPerfil(Integer empleadoId) {
        String query = "SELECT * FROM vw_PerfilEmpleado WHERE empleado_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, empleadoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al obtener perfil del empleado", e);
        }
        return null;
    }

    @Override
    public void actualizarPerfil(Integer empleadoId, String telefono, String direccion, String fotoUrl) {
        String sql = "{CALL sp_ActualizarPerfilEmpleado(?, ?, ?, ?)}";

        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, empleadoId);
            stmt.setString(2, telefono);
            stmt.setString(3, direccion);
            stmt.setString(4, fotoUrl);

            stmt.execute();

        } catch (SQLException e) {
            throw new BaseDatosException("Error al actualizar perfil del empleado", e);
        }
    }

    private PerfilEmpleadoDTO mapearResultSet(ResultSet rs) throws SQLException {
        PerfilEmpleadoDTO dto = new PerfilEmpleadoDTO();

        // Empleado
        dto.setEmpleadoId(rs.getInt("empleado_id"));
        dto.setCodigoEmpleado(rs.getString("codigo_empleado"));
        dto.setNombres(rs.getString("nombres"));
        dto.setApellidos(rs.getString("apellidos"));
        dto.setDni(rs.getString("dni"));
        dto.setTelefono(rs.getString("telefono"));
        dto.setDireccion(rs.getString("direccion"));

        Date fechaNac = rs.getDate("fecha_nacimiento");
        if (fechaNac != null) dto.setFechaNacimiento(fechaNac.toLocalDate());

        Date fechaIng = rs.getDate("fecha_ingreso");
        if (fechaIng != null) dto.setFechaIngreso(fechaIng.toLocalDate());

        dto.setFotoUrl(rs.getString("foto_url"));
        dto.setEstadoEmpleado(rs.getString("estado_empleado"));
        dto.setDiasVacacionesDisponibles(rs.getInt("dias_vacaciones_disponibles"));

        // Usuario
        dto.setUsuarioId(rs.getInt("usuario_id"));
        dto.setUsername(rs.getString("username"));
        dto.setEmail(rs.getString("email"));
        dto.setRol(rs.getString("rol"));

        Timestamp ultimoAcceso = rs.getTimestamp("ultimo_acceso");
        if (ultimoAcceso != null) dto.setUltimoAcceso(ultimoAcceso.toLocalDateTime());

        // Contrato (puede ser null por LEFT JOIN)
        dto.setCargo(rs.getString("cargo"));
        dto.setArea(rs.getString("area"));
        dto.setTipoContrato(rs.getString("tipo_contrato"));

        double sueldo = rs.getDouble("sueldo_base");
        if (!rs.wasNull()) dto.setSueldoBase(sueldo);

        Date fechaInicioC = rs.getDate("fecha_inicio_contrato");
        if (fechaInicioC != null) dto.setFechaInicioContrato(fechaInicioC.toLocalDate());

        Date fechaFinC = rs.getDate("fecha_fin_contrato");
        if (fechaFinC != null) dto.setFechaFinContrato(fechaFinC.toLocalDate());

        dto.setSistemaPension(rs.getString("sistema_pension"));

        return dto;
    }
}
