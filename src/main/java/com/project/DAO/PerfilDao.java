package com.project.DAO;

import com.project.models.PerfilEmpleadoDTO;

public interface PerfilDao {
    PerfilEmpleadoDTO obtenerPerfil(Integer empleadoId);

    void actualizarPerfil(Integer empleadoId, String telefono, String direccion, String fotoUrl);
}
