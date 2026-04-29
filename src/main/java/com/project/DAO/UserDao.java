package com.project.DAO;

import com.project.models.Usuario;

import java.util.List;

public interface UserDao {

    List<Usuario> getAllUsers();

    void crearUsuario(Usuario usuario);

    Usuario obtenerPorUsername(String userName);
}
