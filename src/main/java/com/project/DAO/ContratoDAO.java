package com.project.DAO;

import com.project.models.Contrato;

public interface ContratoDAO {

    // devuelve contraro activo 
    Contrato obtenerActivoPorEmpleado(Integer empleadoId);
}
