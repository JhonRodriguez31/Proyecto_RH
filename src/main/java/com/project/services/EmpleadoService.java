package com.project.services;

import com.project.models.Empleado;

import java.util.List;

public interface EmpleadoService {
    List<Empleado> obtenerEmpleados();

    Empleado obtenerEmpleado(Integer id);

    void registrarEmpleado(Empleado empleado, Integer usuarioId);

    void actualizarEmpleado(Empleado empleado, Integer usuarioId);

    void eliminarEmpleado(Integer id);

    String generarSiguienteCodigo();
}
