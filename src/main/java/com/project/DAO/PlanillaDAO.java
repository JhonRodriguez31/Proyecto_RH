package com.project.DAO;

import java.util.List;

import com.project.models.Empleado;
import com.project.models.Planilla;

public interface PlanillaDAO {
	
    List<Planilla> listar();

    Planilla obtenerPorId(Integer id);

    Integer registrar(Planilla planilla);

    void actualizar(Planilla planilla);

    void eliminar(Integer id);

}
