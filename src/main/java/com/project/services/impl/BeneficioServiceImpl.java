package com.project.services.impl;

import com.project.DAO.BeneficioDAO;
import com.project.models.Beneficio;
import com.project.models.Estudios;
import com.project.models.Familiar;
import com.project.models.Salud;
import com.project.services.BeneficioService;

import java.util.ArrayList;
import java.util.List;

public class BeneficioServiceImpl implements BeneficioService {
    //Inyeccion de dependencias
    private BeneficioDAO beneficioDAO = null;
    public BeneficioServiceImpl() {
        this.beneficioDAO = beneficioDAO;
    }

    @Override
    public List<Beneficio> obtenerTodos() {
        List<Beneficio> lista = new ArrayList<>();
        lista.add(new Salud());
        lista.add(new Familiar());
        lista.add(new Estudios());
        return lista;
    }

    @Override
    public void guardarBeneficio(Beneficio beneficio) throws Exception {
        if (beneficio.getNombre() == null || beneficio.getNombre().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío");
        }
        System.out.println("Guardando beneficio: " + beneficio.getNombre());
    }
}
