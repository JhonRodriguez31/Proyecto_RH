package com.project.services;

import java.util.List;

import com.project.models.Empleado;
import com.project.models.Planilla;
import com.project.models.PlanillaDetalle;

public interface PlanillaService {
	
	List<Planilla> listarPlanillas();

    Planilla obtenerPlanilla(Integer id);

    List<PlanillaDetalle> obtenerDetalles(Integer planillaId);

    Integer registrarPlanilla(
        Planilla planilla,
        List<PlanillaDetalle> detalles,
        Integer usuarioId
    );

    void actualizarPlanilla(
        Planilla planilla,
        List<PlanillaDetalle> detalles,
        Integer usuarioId
    );

    void eliminarPlanilla(Integer id);
}
