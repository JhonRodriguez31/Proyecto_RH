package com.project.models;

import com.project.common.audit.EntidadAuditable;
import com.project.common.enums.Estado;

import java.time.LocalDate;

public class Empleado extends EntidadAuditable {
    public static final String DEFAULT_PHOTO_URL = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-usuario-vector.jpg";

    private Integer id;
    private String codigoEmpleado;
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private LocalDate fechaIngreso;
    private String fotoUrl = DEFAULT_PHOTO_URL;
    private Estado estado;
    private Integer diasVacacionesDisponibles;


    public Empleado() {
        super();
    }

    public Empleado(String codigoEmpleado, String nombres, String apellidos, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this.codigoEmpleado = codigoEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaIngreso = fechaIngreso;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        if (fotoUrl != null && !fotoUrl.trim().isEmpty()) {
            this.fotoUrl = fotoUrl;
        } else {
            this.fotoUrl = DEFAULT_PHOTO_URL;
        }
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getDiasVacacionesDisponibles() {
        return diasVacacionesDisponibles;
    }

    public void setDiasVacacionesDisponibles(Integer diasVacacionesDisponibles) {
        this.diasVacacionesDisponibles = diasVacacionesDisponibles;
    }
}
