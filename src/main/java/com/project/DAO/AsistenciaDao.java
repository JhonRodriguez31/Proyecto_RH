package com.project.DAO;

import com.project.models.Asistencia;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaDao {

    void registrarEntrada(Asistencia asistencia);

    void registrarSalida(Integer empleadoId, LocalDate fecha);

    Asistencia obtenerPorEmpleadoYFecha(Integer empleadoId, LocalDate fecha);

    List<Asistencia> obtenerHistorialPorEmpleado(Integer empleadoId);

    List<Asistencia> obtenerAsistenciasDeHoy();

    void justificarAsistencia(Integer asistenciaId, String motivo, Integer adminId);

    void corregirAsistencia(Asistencia asistencia, Integer adminId);
}