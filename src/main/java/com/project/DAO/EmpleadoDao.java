package com.project.DAO;

import com.project.models.Empleado;

import java.util.List;

public interface EmpleadoDao {
    List<Empleado> obtenerEmpleados();

    Empleado obtenerEmpleado(Integer id);

    void registrarEmpleado(Empleado empleado, Integer usuarioId);

    void eliminarEmpleado(Integer id);

    void actualizarEmpleado(Empleado empleado, Integer usuarioId);

    String obtenerUltimoCodigo();
}
