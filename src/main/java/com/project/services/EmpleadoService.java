package com.project.services;

import com.project.models.Empleado;

import java.util.List;

public interface EmpleadoService {
    List<Empleado> obtenerEmpleados();

    Empleado obtenerEmpleado(Integer id);

    void registrarEmpleado(Empleado empleado);

    void eliminarEmpleado(Integer id);
}
