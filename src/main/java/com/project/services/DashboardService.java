package com.project.services;

import java.util.Map;

public interface DashboardService {
    int getTotalEmployees();
    int getActiveContractsCount();
    double getMonthlyPayrollTotal();
    Map<String, Integer> getEmployeesByArea();
    Map<String, Integer> getAttendanceStats();
}
