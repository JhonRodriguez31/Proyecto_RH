package com.project.models;

public class Salud extends Beneficio {

    public Salud() {
        this.nombre = "Seguro Médico EPS";
        this.monto = 150.0;
    }

    @Override
    public String obtenerDetalleEspecial() {
        return "Incluye cobertura dental y oftalmológica para el trabajador.";
    }
}