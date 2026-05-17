package com.project.services.impl;

import com.project.DAO.UserDao;
import com.project.DAO.impl.UserDaoImpl;
import com.project.models.Usuario;
import com.project.services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public List<Usuario> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        userDao.crearUsuario(usuario);
    }
}
