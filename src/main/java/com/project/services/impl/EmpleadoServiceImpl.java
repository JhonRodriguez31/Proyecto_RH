package com.project.services.impl;

import com.project.DAO.EmpleadoDao;
import com.project.models.Empleado;
import com.project.models.Usuario;
import com.project.services.EmpleadoService;
import com.project.services.UserService;
import com.project.services.EmailService;
import com.project.common.enums.Role;
import com.project.config.BcryptFactory;

import java.util.List;

public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoDao empleadoDao;
    private final UserService userService;
    private final EmailService emailService;

    public EmpleadoServiceImpl(EmpleadoDao empleadoDao, UserService userService, EmailService emailService) {
        this.empleadoDao = empleadoDao;
        this.userService = userService;
        this.emailService = emailService;
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
    public void registrarEmpleado(Empleado empleado, String email, Integer usuariId) {
        if (empleado.getNombres() == null || empleado.getNombres().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (empleado.getApellidos() == null || empleado.getApellidos().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (empleado.getDni() == null || empleado.getDni().isBlank()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El Correo es obligatorio");
        }
        if (empleado.getTelefono() == null || empleado.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El Telefono es obligatorio");
        }


        // 1. Registrar el empleado en la base de datos
        empleadoDao.registrarEmpleado(empleado, usuariId);

        // 2. Crear el usuario para el sistema
        String passwordTemporal = BcryptFactory.generarContraseñaTemporal();
        String passwordHashed = BcryptFactory.hashPassword(passwordTemporal);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmpleadoId(empleado.getId());
        nuevoUsuario.setUserName(empleado.getDni()); // El username es su DNI
        nuevoUsuario.setEmail(email); // Usamos el parámetro separado
        nuevoUsuario.setPassword(passwordHashed);
        nuevoUsuario.setRole(Role.USER);
        nuevoUsuario.setPrimeraVez(true);
        nuevoUsuario.setActivo(true);

        userService.crearUsuario(nuevoUsuario);

        // 3. Enviar correo con las credenciales (Asíncrono para no bloquear la UI)
        new Thread(() -> {
            try {
                emailService.enviarCredenciales(email, empleado.getDni(), passwordTemporal);
            } catch (Exception e) {
                System.err.println("Error al enviar correo de bienvenida: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        empleadoDao.eliminarEmpleado(id);
    }

    @Override
    public String generarSiguienteCodigo() {
        String ultimoCodigo = empleadoDao.obtenerUltimoCodigo();
        if (ultimoCodigo == null || !ultimoCodigo.matches("EMP\\d+")) {
            return "EMP001";
        }
        try {
            String numeroParte = ultimoCodigo.substring(3);
            int siguiente = Integer.parseInt(numeroParte) + 1;
            return String.format("EMP%03d", siguiente);
        } catch (NumberFormatException e) {
            return "EMP001";
        }
    }
}
