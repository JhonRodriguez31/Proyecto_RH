package com.project.config;

import com.project.DAO.*;
import com.project.DAO.impl.*;
import com.project.services.*;
import com.project.services.impl.*;

public class ServiceFactory {
    //    Empleado
    private static EmpleadoDao empleadoDao;
    private static EmpleadoService empleadoService;
    //    Autenticacion
    private static AuthDao authDao;
    private static AuthService authService;
    //    User
    private static UserDao userDao;
    private static UserService userService;

    //    Email
    private static EmailService emailService;

    //    Perfil
    private static PerfilDao perfilDao;
    private static PerfilService perfilService;

    //    Report
    private static ReportService reportService;

    //    Dashboard
    private static DashboardDAO dashboardDao;
    private static DashboardService dashboardService;

    // Asistencia
    private static AsistenciaDao asistenciaDao;
    private static AsistenciaService asistenciaService;

    // Vacaciones
    private static VacacionDAO vacacionDao;
    private static VacacionService vacacionService;

    // Planilla
    private static PlanillaDAO planillaDao;
    private static PlanillaDetalleDAO planillaDetalleDao;
    private static PlanillaService planillaService;

    // Imagen
    private static ImageService imageService;

    public static EmpleadoDao getEmpleadoDao() {
        if (empleadoDao == null) {
            empleadoDao = new EmpleadoDaoImpl();
        }
        return empleadoDao;
    }

    public static EmpleadoService getEmpleadoService() {
        if (empleadoService == null) {
            empleadoService = new EmpleadoServiceImpl(getEmpleadoDao(), getUserService(), getEmailService());
        }
        return empleadoService;
    }

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

    public static EmailService getEmailService() {
        if (emailService == null) {
            emailService = new EmailServiceImpl();
        }
        return emailService;
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

    public static VacacionDAO getVacacionDao() {
        if (vacacionDao == null) {
            vacacionDao = new VacacionDAOImpl();
        }
        return vacacionDao;
    }

    public static VacacionService getVacacionService() {
        if (vacacionService == null) {
            vacacionService = new VacacionServiceImpl(getVacacionDao(), getEmpleadoDao());
        }
        return vacacionService;
    }

    public static PlanillaDAO getPlanillaDao() {
        if (planillaDao == null) {
            planillaDao = new PlanillaDAOImpl();
        }
        return planillaDao;
    }

    public static PlanillaDetalleDAO getPlanillaDetalleDao() {
        if (planillaDetalleDao == null) {
            planillaDetalleDao = new PlanillaDetalleDAOImpl();
        }
        return planillaDetalleDao;
    }

    public static PlanillaService getPlanillaService() {
        if (planillaService == null) {
            planillaService = new PlanillaServiceImpl(getPlanillaDao(), getPlanillaDetalleDao(), getEmpleadoDao(), getAsistenciaDao());
        }
        return planillaService;
    }

    public static ImageService getImageService() {
        if (imageService == null) {
            imageService = new ImageServiceImpl();
        }
        return imageService;
    }

    public static DashboardDAO getDashboardDao() {
        if (dashboardDao == null) {
            dashboardDao = new DashboardDAOImpl();
        }
        return dashboardDao;
    }

    public static DashboardService getDashboardService() {
        if (dashboardService == null) {
            dashboardService = new DashboardServiceImpl();
        }
        return dashboardService;
    }
}
