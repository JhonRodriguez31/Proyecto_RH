package com.project.services.impl;

import com.project.DAO.PerfilDao;
import com.project.models.PerfilEmpleadoDTO;
import com.project.services.PerfilService;

public class PerfilServiceImpl implements PerfilService {

    private final PerfilDao perfilDao;

    public PerfilServiceImpl(PerfilDao perfilDao) {
        this.perfilDao = perfilDao;
    }

    @Override
    public PerfilEmpleadoDTO obtenerPerfilCompleto(Integer empleadoId) {
        if (empleadoId == null) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo");
        }
        return perfilDao.obtenerPerfil(empleadoId);
    }

    @Override
    public void actualizarPerfil(Integer empleadoId, String telefono, String direccion, String fotoUrl) {
        if (empleadoId == null) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo");
        }
        perfilDao.actualizarPerfil(empleadoId, telefono, direccion, fotoUrl);
    }
    
    @Override
    public void actualizarPerfil(PerfilEmpleadoDTO dto) {
        if (dto == null || dto.getEmpleadoId() == null) {
            throw new IllegalArgumentException("El DTO del perfil no puede ser nulo");
        }
        perfilDao.actualizarPerfil(dto.getEmpleadoId(), dto.getTelefono(), dto.getDireccion(), dto.getFotoUrl());
    }
    
    
    
    
}
