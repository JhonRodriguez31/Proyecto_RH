package com.project.services.impl;

import com.project.DAO.VacacionDAO;
import com.project.DAO.impl.VacacionDAOImpl;
import com.project.models.Empleado;
import com.project.models.Vacacion;
import com.project.services.VacacionService;

import java.time.LocalDate;
import java.util.List;

public class VacacionServiceImpl implements VacacionService {

    private final VacacionDAO vacacionDAO;

    public VacacionServiceImpl() {
        this.vacacionDAO = new VacacionDAOImpl();
    }

    @Override
    public List<Vacacion> listar() {
        return vacacionDAO.listar();
    }

    @Override
    public List<Vacacion> listarPorEmpleado(Integer empleadoId) {
        return vacacionDAO.listarPorEmpleado(empleadoId);
    }

    @Override
    public void solicitar(Vacacion vacacion, Empleado empleado) {

        // ── Regla 1: fechas obligatorias ──────────────────
        if (vacacion.getFechaInicio() == null || vacacion.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }

        // ── Regla 2: fin no puede ser antes que inicio ────
        if (vacacion.getFechaFin().isBefore(vacacion.getFechaInicio())) {
            throw new IllegalArgumentException(
                    "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // ── Regla 3: calcular días y verificar saldo ──────
        int diasSolicitados = calcularDiasHabiles(
                vacacion.getFechaInicio(), vacacion.getFechaFin());

        if (diasSolicitados <= 0) {
            throw new IllegalArgumentException("El rango de fechas no genera días hábiles.");
        }

        int diasDisponibles = empleado.getDiasVacacionesDisponibles() != null
                ? empleado.getDiasVacacionesDisponibles() : 0;

        if (diasSolicitados > diasDisponibles) {
            throw new IllegalArgumentException(
                    "No tiene suficientes días de vacaciones disponibles. " +
                    "Disponibles: " + diasDisponibles + ", Solicitados: " + diasSolicitados);
        }

        // ── Preparar y persistir ──────────────────────────
        vacacion.setDiasSolicitados(diasSolicitados);
        vacacion.setFechaSolicitud(LocalDate.now());
        vacacion.setEstado("PENDIENTE");
        vacacionDAO.registrar(vacacion);
    }

    @Override
    public void aprobar(Integer vacacionId, Integer revisadoPor) {
        if (vacacionId == null) {
            throw new IllegalArgumentException("Debe seleccionar una vacación.");
        }
        vacacionDAO.aprobar(vacacionId, revisadoPor);
    }

    @Override
    public void rechazar(Integer vacacionId, Integer revisadoPor, String observacion) {
        if (vacacionId == null) {
            throw new IllegalArgumentException("Debe seleccionar una vacación.");
        }
        if (observacion == null || observacion.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo del rechazo.");
        }
        vacacionDAO.rechazar(vacacionId, revisadoPor, observacion);
    }

    // ── Utilidad: días hábiles entre dos fechas ───────────
    // Cuenta lunes a sábado (en Perú se trabaja 6 días).
    // Si tu empresa trabaja 5 días, cambia la condición a != SUNDAY && != SATURDAY
    private int calcularDiasHabiles(LocalDate inicio, LocalDate fin) {
        int dias = 0;
        LocalDate cursor = inicio;
        while (!cursor.isAfter(fin)) {
            switch (cursor.getDayOfWeek()) {
                case SUNDAY -> { /* no cuenta */ }
                default -> dias++;
            }
            cursor = cursor.plusDays(1);
        }
        return dias;
    }
}
