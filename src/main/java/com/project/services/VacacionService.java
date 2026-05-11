package com.project.services;

import com.project.models.Empleado;
import com.project.models.Vacacion;
import java.util.List;

public interface VacacionService {

    /** Lista todas las vacaciones (admin). */
    List<Vacacion> listar();

    /** Lista vacaciones de un empleado específico (user). */
    List<Vacacion> listarPorEmpleado(Integer empleadoId);

    /**
     * Registra una solicitud de vacación.
     * Reglas de negocio aplicadas en el servicio:
     *  - fecha_fin >= fecha_inicio
     *  - días solicitados <= días disponibles del empleado
     */
    void solicitar(Vacacion vacacion, Empleado empleado);

    /** Aprueba la solicitud. Solo ADMIN. */
    void aprobar(Integer vacacionId, Integer revisadoPor);

    /**
     * Rechaza la solicitud. Solo ADMIN.
     * @param observacion motivo del rechazo (obligatorio)
     */
    void rechazar(Integer vacacionId, Integer revisadoPor, String observacion);
}
