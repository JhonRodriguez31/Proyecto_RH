package com.project.services;

import com.project.models.Usuario;

public interface AuthService {
    String crearUsuarioConContraseñaTemporal(Integer empleadoId, String userName, String email, String rol);

    Usuario login(String dni, String password);

    void cambiarContraseña(Integer usuarioId, String newPassword);
}
