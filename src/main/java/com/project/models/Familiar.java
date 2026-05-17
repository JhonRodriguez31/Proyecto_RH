package com.project.models;

public class Familiar extends Beneficio {

    public Familiar() {
        this.nombre = "Asignación Familiar";
        this.monto = 100.0;
    }

    @Override
    public String obtenerDetalleEspecial() {
        return "Aplica para trabajadores con hijos menores de 18 años.";
    }
}