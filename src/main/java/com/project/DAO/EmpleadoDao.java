package com.project.DAO;

import com.project.models.Empleado;

import java.util.List;

public interface EmpleadoDao {
    List<Empleado> obtenerEmpleados();

    Empleado obtenerEmpleado(Integer id);

    void registrarEmpleado(Empleado empleado);

    void eliminarEmpleado(Integer id);

    void actualizarEmpleado(Integer id, Empleado empleado);
}
