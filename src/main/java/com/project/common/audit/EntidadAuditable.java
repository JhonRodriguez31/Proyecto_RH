package com.project.common.audit;

import java.time.LocalDateTime;

public abstract class EntidadAuditable {
    protected LocalDateTime fechaCreacion;
    protected LocalDateTime fechaActualizacion;
    protected Integer creadoPor;
    protected Integer actualizadoPor;

    public EntidadAuditable() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Integer getActualizadoPor() {
        return actualizadoPor;
    }

    public void setActualizadoPor(Integer actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }

    public void marcarActualizado(Integer usuarioId) {
        this.fechaActualizacion = LocalDateTime.now();
        this.actualizadoPor = usuarioId;
    }


    @Override
    public String toString() {
        return "EntidadAuditable{" +
                "fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                ", creadoPor=" + creadoPor +
                ", actualizadoPor=" + actualizadoPor +
                '}';
    }
}
