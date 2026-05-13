package com.project.DAO;

import com.project.models.Vacacion;
import java.util.List;

public interface VacacionDAO {

    /** Todas las vacaciones (admin). */
    List<Vacacion> listar();

    /** Vacaciones de un empleado específico. */
    List<Vacacion> listarPorEmpleado(Integer empleadoId);

    /** Registra solicitud y devuelve el id generado. */
    Integer registrar(Vacacion vacacion);

    /** Aprueba, descuenta días del empleado. */
    void aprobar(Integer vacacionId, Integer revisadoPor);

    /** Rechaza con observación. */
    void rechazar(Integer vacacionId, Integer revisadoPor, String observacion);
}
