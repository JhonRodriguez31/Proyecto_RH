package com.project.models;

import java.time.LocalDate;

public class Vacacion {

    private Integer id;
    private Integer empleadoId;
    private String  nombreEmpleado;   // campo extra para mostrar en tabla
    private LocalDate fechaSolicitud;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer diasSolicitados;
    private String  estado;           // PENDIENTE | APROBADA | RECHAZADA
    private String  observacion;
    private Integer revisadoPor;

    public Vacacion() {}

    // ── Getters / Setters ──────────────────────────────

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Integer getDiasSolicitados() { return diasSolicitados; }
    public void setDiasSolicitados(Integer diasSolicitados) { this.diasSolicitados = diasSolicitados; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public Integer getRevisadoPor() { return revisadoPor; }
    public void setRevisadoPor(Integer revisadoPor) { this.revisadoPor = revisadoPor; }
}
