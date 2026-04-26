package com.project.services.impl;

import com.project.DAO.EmpleadoDao;
import com.project.DAO.impl.EmpleadoDaoImpl;
import com.project.models.Empleado;
import com.project.services.EmpleadoService;

import java.util.List;

public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoDao empleadoDao;

    public EmpleadoServiceImpl(EmpleadoDao empleadoDao) {
        this.empleadoDao = new EmpleadoDaoImpl();
    }

    @Override
    public List<Empleado> obtenerEmpleados() {
        return empleadoDao.obtenerEmpleados();
    }

    @Override
    public Empleado obtenerEmpleado(Integer id) {
        return empleadoDao.obtenerEmpleado(id);
    }

    @Override
    public void registrarEmpleado(Empleado empleado) {
//        if (empleado.getCodigoEmpleado() == null || empleado.getCodigoEmpleado().isBlank()) {
//            throw new IllegalArgumentException("El nombre es obligatorio");
//        }
        if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (empleado.getApellidos() == null || empleado.getApellidos().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (empleado.getDni() == null || empleado.getDni().isBlank()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
        if (empleado.getTelefono() == null || empleado.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El Telefono es obligatorio");
        }


        empleadoDao.registrarEmpleado(empleado);
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        empleadoDao.eliminarEmpleado(id);
    }
}
