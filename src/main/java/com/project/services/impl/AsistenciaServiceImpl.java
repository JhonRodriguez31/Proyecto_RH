package com.project.services.impl;

import com.project.DAO.AsistenciaDao;
import com.project.models.Asistencia;
import com.project.services.AsistenciaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaDao asistenciaDao;
    private static final LocalTime HORA_LIMITE = LocalTime.of(8, 0); // 8:00 AM

    public AsistenciaServiceImpl(AsistenciaDao asistenciaDao) {
        this.asistenciaDao = asistenciaDao;
    }

    @Override
    public void registrarEntrada(Integer empleadoId) {
        // Verificar si ya registró entrada hoy
        Asistencia existente = asistenciaDao.obtenerPorEmpleadoYFecha(empleadoId, LocalDate.now());
        if (existente != null) {
            throw new RuntimeException("El empleado ya registró su entrada hoy");
        }

        LocalDateTime horaActual = LocalDateTime.now();
        LocalTime horaEntrada = horaActual.toLocalTime();

        // Detectar tardanza
        boolean esTardanza = horaEntrada.isAfter(HORA_LIMITE);
        int minutosTardanza = 0;
        String estado = "PRESENTE";

        if (esTardanza) {
            estado = "TARDANZA";
            minutosTardanza = (int) java.time.Duration.between(HORA_LIMITE, horaEntrada).toMinutes();
        }

        Asistencia asistencia = new Asistencia(empleadoId, LocalDate.now(), horaActual, estado);
        asistencia.setMinutosTardanza(minutosTardanza);
        asistenciaDao.registrarEntrada(asistencia);
    }

    @Override
    public void registrarSalida(Integer empleadoId) {
        // Verificar que tenga entrada registrada hoy
        Asistencia existente = asistenciaDao.obtenerPorEmpleadoYFecha(empleadoId, LocalDate.now());
        if (existente == null) {
            throw new RuntimeException("El empleado no tiene entrada registrada hoy");
        }
        if (existente.getHoraSalida() != null) {
            throw new RuntimeException("El empleado ya registró su salida hoy");
        }
        asistenciaDao.registrarSalida(empleadoId, LocalDate.now());
    }

    @Override
    public Asistencia obtenerAsistencia(Integer empleadoId, LocalDate fecha) {
        return asistenciaDao.obtenerPorEmpleadoYFecha(empleadoId, fecha);
    }

    @Override
    public List<Asistencia> obtenerHistorial(Integer empleadoId) {
        return asistenciaDao.obtenerHistorialPorEmpleado(empleadoId);
    }

    @Override
    public List<Asistencia> obtenerAsistenciasDeHoy() {
        return asistenciaDao.obtenerAsistenciasDeHoy();
    }

    @Override
    public void justificarAsistencia(Integer asistenciaId, String motivo, Integer adminId) {
        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("El motivo no puede estar vacío");
        }
        asistenciaDao.justificarAsistencia(asistenciaId, motivo, adminId);
    }

    @Override
    public void corregirAsistencia(Asistencia asistencia, Integer adminId) {
        if (asistencia == null || asistencia.getId() == null) {
            throw new RuntimeException("La asistencia no es válida");
        }
        asistenciaDao.corregirAsistencia(asistencia, adminId);
    }
}