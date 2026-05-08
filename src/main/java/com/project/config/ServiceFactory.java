package com.project.config;

import com.project.DAO.AuthDao;
import com.project.DAO.EmpleadoDao;
import com.project.DAO.UserDao;
import com.project.DAO.impl.AuthDaoImpl;
import com.project.DAO.impl.EmpleadoDaoImpl;
import com.project.DAO.impl.UserDaoImpl;
import com.project.services.AuthService;
import com.project.services.EmpleadoService;
import com.project.services.UserService;
import com.project.services.impl.AuthServiceImpl;
import com.project.services.impl.EmpleadoServiceImpl;
import com.project.services.impl.UserServiceImpl;
import com.project.services.ReportService;
import com.project.services.impl.ReportServiceImpl;

public class ServiceFactory {
    //    Empleado
    private static EmpleadoDao empleadoDao;
    private static EmpleadoService empleadoService;
    //    Autenticacion
    private static AuthDao authDao;
    private static AuthServiceImpl authService;
    //    User
    private static UserDao userDao;
    private static UserService userService;
    //    Report
    private static ReportService reportService;

    public static EmpleadoDao getEmpleadoDao() {
        if (empleadoDao == null) {
            empleadoDao = new EmpleadoDaoImpl();
        }
        return empleadoDao;
    }

    public static EmpleadoService getEmpleadoService() {
        if (empleadoService == null) {
            empleadoService = new EmpleadoServiceImpl(getEmpleadoDao());
        }
        return empleadoService;
    }

//    Authenticacion

    public static AuthDao getAuthDao() {
        if (authDao == null) {
            authDao = new AuthDaoImpl();
        }
        return authDao;
    }

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthServiceImpl(getAuthDao(), getUserDao());
        }
        return authService;
    }

    //    Usuario
    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDaoImpl();
        }
        return userDao;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserServiceImpl(getUserDao());
        }
        return userService;
    }

    public static ReportService getReportService() {
        if (reportService == null) {
            reportService = new ReportServiceImpl();
        }
        return reportService;
    }


}
