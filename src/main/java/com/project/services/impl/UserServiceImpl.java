package com.project.services.impl;

import com.project.DAO.UserDao;
import com.project.DAO.impl.UserDaoImpl;
import com.project.models.User;
import com.project.services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }


    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
