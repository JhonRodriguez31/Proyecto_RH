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
    public void registrarEmpleado(Empleado empleado) {
        String query = "INSERT INTO Empleado (codigo_empleado, nombres, apellidos, dni, telefono, correo, direccion, fecha_nacimiento, fecha_ingreso, foto_url, estado, dias_vacaciones_disponibles) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, empleado.getCodigoEmpleado());
            stmt.setString(2, empleado.getNombre());
            stmt.setString(3, empleado.getApellidos());
            stmt.setString(4, empleado.getDni());
            stmt.setString(5, empleado.getTelefono());
            stmt.setString(6, empleado.getCorreo());
            stmt.setString(7, empleado.getDireccion());
            stmt.setDate(8, java.sql.Date.valueOf(empleado.getFechaNacimiento()));
            stmt.setDate(9, java.sql.Date.valueOf(empleado.getFechaIngreso()));
            stmt.setString(10, empleado.getFotoUrl());
            stmt.setString(11, empleado.getEstado().name());
            stmt.setInt(12, empleado.getDiasVacacionesDisponibles());

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
    public void actualizarEmpleado(Integer id, Empleado empleado) {

    }


    private Empleado mapearResultSet(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        Date sqlDateFecNacimiento = rs.getDate("fecha_nacimiento");
        Date sqlDateFecIngreso = rs.getDate("fecha_ingreso");

        empleado.setId(rs.getInt("id"));
        empleado.setCodigoEmpleado(rs.getString("codigo_empleado"));
        empleado.setNombre(rs.getString("nombres"));
        empleado.setApellidos(rs.getString("apellidos"));
        empleado.setDni(rs.getString("dni"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setCorreo(rs.getString("correo"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setFechaNacimiento(sqlDateFecNacimiento.toLocalDate());
        empleado.setFechaIngreso(sqlDateFecIngreso.toLocalDate());
        empleado.setFotoUrl(rs.getString("foto_url"));
        empleado.setEstado(Estado.valueOf(rs.getString("estado")));
        empleado.setDiasVacacionesDisponibles(rs.getInt("dias_vacaciones_disponibles"));
        return empleado;
    }

}
