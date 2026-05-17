package com.project.DAO;

import java.util.List;

import com.project.models.PlanillaDetalle;

public interface PlanillaDetalleDAO {

	List<PlanillaDetalle> listarPorPlanilla(Integer planillaId);

    void registrarDetalle(PlanillaDetalle detalle);

    void eliminarPorPlanilla(Integer planillaId);
}
