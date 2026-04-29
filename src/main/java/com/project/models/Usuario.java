package com.project.models;

import com.project.common.audit.EntidadAuditable;
import com.project.common.enums.Role;

import java.time.LocalDateTime;

public class Usuario extends EntidadAuditable {
    private Integer id;
    private Integer empleadoId;
    private Boolean primeraVez;
    private Role role;
    private String userName;
    private String password;
    private String email;
    private LocalDateTime bloqueadoHasta;
    private Boolean activo;
    private LocalDateTime ultimoAcceso;

    public Usuario() {
        super();
    }

    public Usuario(Integer empleadoId, String userName, String password, String email, Role rol) {
        super();
        this.empleadoId = empleadoId;
        this.role = rol;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.primeraVez = true;
        this.activo = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public Boolean getPrimeraVez() {
        return primeraVez;
    }

    public void setPrimeraVez(Boolean primeraVez) {
        this.primeraVez = primeraVez;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) {
        this.bloqueadoHasta = bloqueadoHasta;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
}
