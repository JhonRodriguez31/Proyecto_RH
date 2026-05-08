package com.project.common.util;

import com.project.models.Usuario;

public class SessionManager {
    private static Usuario usuarioLogueado;

    public static void login(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    public static void logout() {
        usuarioLogueado = null;
    }

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static Integer getUsuarioId() {
        return (usuarioLogueado != null) ? usuarioLogueado.getId() : 1; // Default to 1 if no session (for testing/safety)
    }
}
