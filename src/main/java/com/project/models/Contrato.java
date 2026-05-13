package com.project.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Contrato {

    private Integer   id;
    private Integer   empleadoId;
    private String    cargo;
    private String    area;
    private String    tipoContrato;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal sueldoBase;
    private String    sistemaPension;  // AFP | ONP | null
    private String    estado;

    public Contrato() {}

    // ── Getters / Setters ──────────────────────────────

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public BigDecimal getSueldoBase() { return sueldoBase; }
    public void setSueldoBase(BigDecimal sueldoBase) { this.sueldoBase = sueldoBase; }

    public String getSistemaPension() { return sistemaPension; }
    public void setSistemaPension(String sistemaPension) { this.sistemaPension = sistemaPension; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
