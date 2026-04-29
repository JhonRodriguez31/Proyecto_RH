package com.project.DAO.impl;

import com.project.DAO.EmpleadoDao;
import com.project.common.enums.Estado;
import com.project.common.exception.BaseDatosException;
import com.project.common.exception.EmpleadoNotFoundException;
import com.project.config.DatabaseConfig;
import com.project.models.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDaoImpl implements EmpleadoDao {
    @Override
    public List<Empleado> obtenerEmpleados() {

        List<Empleado> empleados = new ArrayList<>();
        String query = "SELECT * FROM Empleado";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                Empleado empleado = mapearResultSet(rs);
                empleados.add(empleado);
            }
        } catch (
                SQLException e) {

            throw new BaseDatosException("Error al obtener empleados", e);
        }
        return empleados;
    }

    @Override
    public Empleado obtenerEmpleado(Integer id) {
        String query = "SELECT * FROM Empleado WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Empleado empleado = mapearResultSet(rs);
                    return empleado;
                }
                throw new EmpleadoNotFoundException("Empleado con ID" + id + "no existe");
            }
        } catch (
                SQLException e) {
            throw new BaseDatosException("Error al obtener empleado", e);
        }
    }


    @Override
    public void registrarEmpleado(Empleado empleado, Integer usuarioId) {
        String query = "INSERT INTO dbo.Empleado " +
                "(codigo_empleado, nombres, apellidos, dni, telefono, " +
                "direccion, fecha_nacimiento, fecha_ingreso, foto_url, estado, " +
                "dias_vacaciones_disponibles, fecha_creacion, creado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, empleado.getCodigoEmpleado());
            stmt.setString(2, empleado.getNombres());
            stmt.setString(3, empleado.getApellidos());
            stmt.setString(4, empleado.getDni());
            stmt.setString(5, empleado.getTelefono());
            stmt.setString(7, empleado.getDireccion());
            stmt.setDate(8, Date.valueOf(empleado.getFechaNacimiento()));
            stmt.setDate(9, Date.valueOf(empleado.getFechaIngreso()));
            stmt.setString(10, empleado.getFotoUrl());
            stmt.setString(11, empleado.getEstado().name());
            stmt.setInt(12, empleado.getDiasVacacionesDisponibles());

            // Auditoría
            stmt.setObject(13, Timestamp.valueOf(empleado.getFechaCreacion()));
            stmt.setInt(14, usuarioId);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new BaseDatosException("Error al crear empleado", null);
            }
        } catch (
                SQLException e) {
            throw new BaseDatosException("Error al crear empleado", e);
        }
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        String sql = "DELETE FROM EMPLEADO where id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new EmpleadoNotFoundException("Empleado con ID : " + id + "no existe");
            }

        } catch (
                SQLException e) {
            throw new BaseDatosException("Error al eliminar usuario con ID : " + id, e);
        }
    }

    @Override
    public void actualizarEmpleado(Empleado empleado, Integer usuarioId) {
        String query = "UPDATE dbo.Empleado SET " +
                "nombres = ?, apellidos = ?, telefono = ?, " +
                "direccion = ?, foto_url = ?, estado = ?, " +
                "fecha_actualizacion = SYSDATETIME(), " +
                "actualizado_por = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, empleado.getNombres());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getTelefono());
            stmt.setString(5, empleado.getDireccion());
            stmt.setString(6, empleado.getFotoUrl());
            stmt.setString(7, empleado.getEstado().name());
            stmt.setInt(8, usuarioId);
            stmt.setInt(9, empleado.getId());

            stmt.executeUpdate();
        } catch (
                SQLException e) {
            throw new RuntimeException("Error al actualizar empleado", e);
        }
    }


    private Empleado mapearResultSet(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        Date sqlDateFecNacimiento = rs.getDate("fecha_nacimiento");
        Date sqlDateFecIngreso = rs.getDate("fecha_ingreso");

        empleado.setId(rs.getInt("id"));
        empleado.setCodigoEmpleado(rs.getString("codigo_empleado"));
        empleado.setNombres(rs.getString("nombres"));
        empleado.setApellidos(rs.getString("apellidos"));
        empleado.setDni(rs.getString("dni"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setFechaNacimiento(sqlDateFecNacimiento.toLocalDate());
        empleado.setFechaIngreso(sqlDateFecIngreso.toLocalDate());
        empleado.setFotoUrl(rs.getString("foto_url"));
        empleado.setEstado(Estado.valueOf(rs.getString("estado")));
        empleado.setDiasVacacionesDisponibles(rs.getInt("dias_vacaciones_disponibles"));
        return empleado;
    }

}
