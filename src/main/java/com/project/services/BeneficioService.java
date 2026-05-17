package com.project.services;

import com.project.models.Beneficio;

import java.util.List;

public interface BeneficioService {
    List<Beneficio> obtenerTodos();
    void guardarBeneficio(Beneficio beneficio) throws Exception;
}
