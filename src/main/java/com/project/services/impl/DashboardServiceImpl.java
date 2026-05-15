package com.project.services.impl;

import com.project.DAO.DashboardDAO;
import com.project.DAO.impl.DashboardDAOImpl;
import com.project.services.DashboardService;
import java.util.Map;

public class DashboardServiceImpl implements DashboardService {
    private final DashboardDAO dashboardDAO;

    public DashboardServiceImpl() {
        this.dashboardDAO = new DashboardDAOImpl();
    }

    @Override
    public int getTotalEmployees() {
        return dashboardDAO.getTotalEmployees();
    }

    @Override
    public int getActiveContractsCount() {
        return dashboardDAO.getActiveContractsCount();
    }

    @Override
    public double getMonthlyPayrollTotal() {
        return dashboardDAO.getMonthlyPayrollTotal();
    }

    @Override
    public Map<String, Integer> getEmployeesByArea() {
        return dashboardDAO.getEmployeesByArea();
    }

    @Override
    public Map<String, Integer> getAttendanceStats() {
        return dashboardDAO.getAttendanceStats();
    }
}
