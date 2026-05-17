package com.project.services.impl;

import com.project.DAO.AuthDao;
import com.project.DAO.UserDao;
import com.project.DAO.impl.AuthDaoImpl;
import com.project.DAO.impl.UserDaoImpl;
import com.project.config.BcryptFactory;
import com.project.models.Usuario;
import com.project.services.AuthService;

public class AuthServiceImpl implements AuthService {


    private final AuthDao authDao;
    private Usuario usuarioAutenticado;

    public AuthServiceImpl(AuthDao authDao, UserDao userDao) {
//        this.userDao = userDao;
        this.authDao = authDao;
    }

    @Override
    public String crearUsuarioConContraseñaTemporal(Integer empleadoId, String userName, String email, String rol) {


        String passwordTemporal = BcryptFactory.generarContraseñaTemporal();
        String passwordHasheada = BcryptFactory.hashPassword(passwordTemporal);
        return "";
    }

    @Override
    public Usuario login(String dni, String password) {
        Usuario usuario = authDao.encontrarPorDni(dni);
        if (usuario != null && BcryptFactory.verifyPasswors(password, usuario.getPassword())) {
            this.usuarioAutenticado = usuario;
            return usuario;

        }
        return null;
    }

    public Usuario obtenerUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    @Override
    public void logout() {
        this.usuarioAutenticado = null;
    }

    @Override
    public void cambiarContraseña(Integer usuarioId, String newPassword) {
        String passwordHashed = BcryptFactory.hashPassword(newPassword);
        authDao.actualizarPassword(usuarioId, passwordHashed, false);
        
        // Actualizar el estado del usuario en la sesión si es el actual
        if (usuarioAutenticado != null && usuarioAutenticado.getId().equals(usuarioId)) {
            usuarioAutenticado.setPassword(passwordHashed);
            usuarioAutenticado.setPrimeraVez(false);
        }
    }
}
