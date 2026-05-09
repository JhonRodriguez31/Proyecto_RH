package com.project.services;

import com.project.models.Asistencia;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaService {

    // Registrar entrada de un empleado
    void registrarEntrada(Integer empleadoId);

    // Registrar salida de un empleado
    void registrarSalida(Integer empleadoId);

    // Obtener asistencia de un empleado en una fecha
    Asistencia obtenerAsistencia(Integer empleadoId, LocalDate fecha);

    // Obtener historial de asistencias de un empleado
    List<Asistencia> obtenerHistorial(Integer empleadoId);

    // Obtener todas las asistencias de hoy
    List<Asistencia> obtenerAsistenciasDeHoy();

    // Justificar una falta o tardanza
    void justificarAsistencia(Integer asistenciaId, String motivo, Integer adminId);

    // Corregir un registro de asistencia (solo admin)
    void corregirAsistencia(Asistencia asistencia, Integer adminId);
}