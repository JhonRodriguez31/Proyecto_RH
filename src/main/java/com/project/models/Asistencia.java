package com.project.models;

import com.project.common.audit.EntidadAuditable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Asistencia extends EntidadAuditable {

    private Integer id;
    private Integer empleadoId;
    private LocalDate fecha;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private String estado; // presente, tarde, falta, justificado
    private Integer minutosTardanza;
    private String motivoJustificacion;
    private Integer corregidoPor;
    private String observacion;

    public Asistencia() {
        super();
    }

    public Asistencia(Integer empleadoId, LocalDate fecha, LocalDateTime horaEntrada, String estado) {
        this.empleadoId = empleadoId;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.estado = estado;
        this.minutosTardanza = 0;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalDateTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getMinutosTardanza() { return minutosTardanza; }
    public void setMinutosTardanza(Integer minutosTardanza) { this.minutosTardanza = minutosTardanza; }

    public String getMotivoJustificacion() { return motivoJustificacion; }
    public void setMotivoJustificacion(String motivoJustificacion) { this.motivoJustificacion = motivoJustificacion; }

    public Integer getCorregidoPor() { return corregidoPor; }
    public void setCorregidoPor(Integer corregidoPor) { this.corregidoPor = corregidoPor; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    @Override
    public String toString() {
        return "Asistencia{" +
                "id=" + id +
                ", empleadoId=" + empleadoId +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                '}';
    }
}