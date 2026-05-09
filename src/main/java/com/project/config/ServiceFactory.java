package com.project.config;

import com.project.DAO.AsistenciaDao;
import com.project.DAO.impl.AsistenciaDaoImpl;
import com.project.services.AsistenciaService;
import com.project.services.impl.AsistenciaServiceImpl;
import com.project.DAO.AuthDao;
import com.project.DAO.EmpleadoDao;
import com.project.DAO.PerfilDao;
import com.project.DAO.UserDao;
import com.project.DAO.impl.AuthDaoImpl;
import com.project.DAO.impl.EmpleadoDaoImpl;
import com.project.DAO.impl.PerfilDaoImpl;
import com.project.DAO.impl.UserDaoImpl;
import com.project.services.AuthService;
import com.project.services.EmpleadoService;
import com.project.services.PerfilService;
import com.project.services.UserService;
import com.project.services.impl.AuthServiceImpl;
import com.project.services.impl.EmpleadoServiceImpl;
import com.project.services.impl.PerfilServiceImpl;
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

    //    Perfil
    private static PerfilDao perfilDao;
    private static PerfilService perfilService;

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


    public static PerfilDao getPerfilDao() {
        if (perfilDao == null) {
            perfilDao = new PerfilDaoImpl();
        }
        return perfilDao;
    }

    public static PerfilService getPerfilService() {
        if (perfilService == null) {
            perfilService = new PerfilServiceImpl(getPerfilDao());
        }
        return perfilService;
    }

    public static ReportService getReportService() {
        if (reportService == null) {
            reportService = new ReportServiceImpl();
        }
        return reportService;
    }
    // Asistencia
    private static AsistenciaDao asistenciaDao;
    private static AsistenciaService asistenciaService;

    public static AsistenciaDao getAsistenciaDao() {
        if (asistenciaDao == null) {
            asistenciaDao = new AsistenciaDaoImpl();
        }
        return asistenciaDao;
    }

    public static AsistenciaService getAsistenciaService() {
        if (asistenciaService == null) {
            asistenciaService = new AsistenciaServiceImpl(getAsistenciaDao());
        }
        return asistenciaService;
    }


}
