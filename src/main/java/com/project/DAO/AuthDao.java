package com.project.DAO;

import com.project.models.Usuario;

public interface AuthDao {
    Usuario encontrarPorDni(String dni);

    void actualizarPassword(Integer usuarioId, String hashedPassword, boolean primeraVez);
}
