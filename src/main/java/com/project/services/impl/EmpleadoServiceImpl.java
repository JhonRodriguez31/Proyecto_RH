package com.project.services.impl;

import com.project.DAO.EmpleadoDao;
import com.project.DAO.impl.EmpleadoDaoImpl;
import com.project.models.Empleado;
import com.project.services.EmpleadoService;

import java.util.List;

public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoDao empleadoDao;

    public EmpleadoServiceImpl(EmpleadoDao empleadoDao) {
        this.empleadoDao = empleadoDao;
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
    public void actualizarEmpleado(Empleado empleado, Integer usuarioId) {
        if (empleado.getNombres() == null || empleado.getNombres().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (empleado.getApellidos() == null || empleado.getApellidos().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (empleado.getDni() == null || empleado.getDni().isBlank()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
        empleadoDao.actualizarEmpleado(empleado, usuarioId);
    }

    @Override
    public void registrarEmpleado(Empleado empleado, Integer usuariId) {
//        if (empleado.getCodigoEmpleado() == null || empleado.getCodigoEmpleado().isBlank()) {
//            throw new IllegalArgumentException("El nombre es obligatorio");
//        }
        if (empleado.getNombres() == null || empleado.getNombres().isBlank()) {
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


        empleadoDao.registrarEmpleado(empleado, usuariId);
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        empleadoDao.eliminarEmpleado(id);
    }

    @Override
    public String generarSiguienteCodigo() {
        String ultimoCodigo = empleadoDao.obtenerUltimoCodigo();
        if (ultimoCodigo == null || !ultimoCodigo.startsWith("EMP-")) {
            return "EMP-001";
        }
        try {
            String numeroParte = ultimoCodigo.substring(4);
            int siguiente = Integer.parseInt(numeroParte) + 1;
            return String.format("EMP-%03d", siguiente);
        } catch (NumberFormatException e) {
            return "EMP-001";
        }
    }
}
