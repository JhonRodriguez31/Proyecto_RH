package com.project.DAO.impl;

import com.project.DAO.DashboardDAO;
import com.project.config.DatabaseConfig;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DashboardDAOImpl implements DashboardDAO {
    @Override
    public int getTotalEmployees() {
        String sql = "SELECT COUNT(*) FROM Empleado";
        return executeCountQuery(sql);
    }

    @Override
    public int getActiveContractsCount() {
        String sql = "SELECT COUNT(*) FROM Contrato WHERE estado = 'ACTIVO'";
        return executeCountQuery(sql);
    }

    @Override
    public double getMonthlyPayrollTotal() {
        String sql = "SELECT SUM(sueldo_base) FROM Contrato WHERE estado = 'ACTIVO'";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    @Override
    public Map<String, Integer> getEmployeesByArea() {
        String sql = "SELECT area, COUNT(*) as total FROM Contrato WHERE estado = 'ACTIVO' GROUP BY area";
        return executeGroupedCountQuery(sql, "area");
    }

    @Override
    public Map<String, Integer> getAttendanceStats() {
        String sql = "SELECT estado, COUNT(*) as total FROM Asistencia WHERE fecha = CAST(GETDATE() AS DATE) GROUP BY estado";
        return executeGroupedCountQuery(sql, "estado");
    }

    private int executeCountQuery(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private Map<String, Integer> executeGroupedCountQuery(String sql, String columnName) {
        Map<String, Integer> result = new HashMap<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String key = rs.getString(columnName);
                if (key == null) key = "N/A";
                result.put(key, rs.getInt("total"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }
}
