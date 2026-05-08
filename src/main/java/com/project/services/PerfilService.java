package com.project.services;

import com.project.models.PerfilEmpleadoDTO;

public interface PerfilService {
    PerfilEmpleadoDTO obtenerPerfil(Integer empleadoId);

    void actualizarPerfil(Integer empleadoId, String telefono, String direccion, String fotoUrl);
}
