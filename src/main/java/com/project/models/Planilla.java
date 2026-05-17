package com.project.models;

import java.math.BigDecimal;

/**
 * Cabecera de planilla.
 * Los campos nombreEmpleado, sueldoBase y cargo son "extra"
 * que el DAO llena desde el JOIN para evitar otra consulta.
 */
public class Planilla {

    private int    id;
    private int    empleado_id;
    private int    contrato_id;
    private String periodo;          // formato YYYY-MM
    private String fecha_generacion;
    private String estado;           // GENERADA | PAGADA | ANULADA

    // Campos extra para la vista (no están en BD, se llenan desde JOIN)
    private String     nombreEmpleado;
    private BigDecimal sueldoBase;
    private String     cargo;

    public Planilla() {}

    public Planilla(int id, int empleado_id, int contrato_id,
                    String periodo, String fecha_generacion, String estado) {
        this.id = id;
        this.empleado_id = empleado_id;
        this.contrato_id = contrato_id;
        this.periodo = periodo;
        this.fecha_generacion = fecha_generacion;
        this.estado = estado;
    }

    // ── Getters / Setters ──────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmpleado_id() { return empleado_id; }
    public void setEmpleado_id(int empleado_id) { this.empleado_id = empleado_id; }

    public int getContrato_id() { return contrato_id; }
    public void setContrato_id(int contrato_id) { this.contrato_id = contrato_id; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getFecha_generacion() { return fecha_generacion; }
    public void setFecha_generacion(String fecha_generacion) { this.fecha_generacion = fecha_generacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public BigDecimal getSueldoBase() { return sueldoBase; }
    public void setSueldoBase(BigDecimal sueldoBase) { this.sueldoBase = sueldoBase; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}
