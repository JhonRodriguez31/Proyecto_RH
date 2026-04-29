package com.project.services.impl;

import com.project.DAO.UserDao;
import com.project.DAO.impl.UserDaoImpl;
import com.project.config.BcryptFactory;
import com.project.models.Usuario;
import com.project.services.AuthService;

public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    public AuthServiceImpl(UserDao userDao) {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public String crearUsuarioConContraseñaTemporal(Integer empleadoId, String userName, String email, String rol) {


        String passwordTemporal = BcryptFactory.generarContraseñaTemporal();
        String passwordHasheada = BcryptFactory.hashPassword(passwordTemporal);
        return "";
    }

    @Override
    public Usuario login(String dni, String password) {
        return null;
    }

    @Override
    public void cambiarContraseña(Integer usuarioId, String newPassword) {

    }
}
