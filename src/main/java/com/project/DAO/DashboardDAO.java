package com.project.DAO;

import java.util.Map;

public interface DashboardDAO {
    int getTotalEmployees();
    int getActiveContractsCount();
    double getMonthlyPayrollTotal();
    Map<String, Integer> getEmployeesByArea();
    Map<String, Integer> getAttendanceStats();
}
