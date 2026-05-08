package com.project.services;

import com.project.models.Empleado;
import java.util.List;

public interface ReportService {
    void generarReporteEmpleados(List<Empleado> empleados) throws Exception;
    void generarFichaEmpleado(Empleado empleado) throws Exception;
}
