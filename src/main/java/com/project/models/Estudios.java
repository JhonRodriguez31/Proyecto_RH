package com.project.models;

public class Estudios extends Beneficio {

    public Estudios() {
        this.nombre = "Bono de Capacitación";
        this.monto = 250.0;
    }

    @Override
    public String obtenerDetalleEspecial() {
        return "Válido para cursos técnicos, diplomados y certificaciones.";
    }
}