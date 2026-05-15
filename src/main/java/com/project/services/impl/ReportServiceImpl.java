package com.project.services.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import com.project.common.util.NotificacionService;
import com.project.models.Empleado;
import com.project.services.ReportService;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportServiceImpl implements ReportService {

    public ReportServiceImpl() {
    }

    @Override
    public void generarReporteEmpleados(List<Empleado> empleados, String outputPath) throws Exception {
        String template = loadTemplate("/com/project/templates/empleados_list.html");
        StringBuilder rows = new StringBuilder();

        for (Empleado p : empleados) {
            rows.append("<tr>")
                .append("<td>").append(p.getCodigoEmpleado() != null ? p.getCodigoEmpleado() : "").append("</td>")
                .append("<td>").append(p.getDni() != null ? p.getDni() : "").append("</td>")
                .append("<td>").append(p.getNombres() != null ? p.getNombres() : "").append("</td>")
                .append("<td>").append(p.getApellidos() != null ? p.getApellidos() : "").append("</td>")
                .append("<td>").append(p.getTelefono() != null ? p.getTelefono() : "").append("</td>")
                .append("</tr>");
        }

        String htmlContent = template
                .replace("{{FECHA}}", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()))
                .replace("{{ROWS}}", rows.toString());

        generatePdfFromHtml(htmlContent, outputPath);
        NotificacionService.exito("Reporte generado en: " + outputPath);
        openPdf(outputPath);
    }

    @Override
    public void generarFichaEmpleado(Empleado empleado, String outputPath) throws Exception {
        String template = loadTemplate("/com/project/templates/empleado_detail.html");

        String htmlContent = template
                .replace("{{FECHA}}", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()))
                .replace("{{CODIGO}}", empleado.getCodigoEmpleado() != null ? empleado.getCodigoEmpleado() : "")
                .replace("{{DNI}}", empleado.getDni() != null ? empleado.getDni() : "")
                .replace("{{NOMBRES}}", empleado.getNombres() != null ? empleado.getNombres() : "")
                .replace("{{APELLIDOS}}", empleado.getApellidos() != null ? empleado.getApellidos() : "")
                .replace("{{TELEFONO}}", empleado.getTelefono() != null ? empleado.getTelefono() : "");

        generatePdfFromHtml(htmlContent, outputPath);
        NotificacionService.exito("Ficha individual generada en: " + outputPath);
        openPdf(outputPath);
    }

    private String loadTemplate(String resourcePath) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("No se encontró la plantilla HTML: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void generatePdfFromHtml(String htmlContent, String outputPath) throws Exception {
        try (OutputStream os = new FileOutputStream(outputPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useSVGDrawer(new BatikSVGDrawer());
            builder.withHtmlContent(htmlContent, getClass().getResource("/com/project/templates/").toExternalForm());
            builder.toStream(os);
            builder.run();
        }
    }

    private void openPdf(String filePath) {
        try {
            File pdfFile = new File(filePath);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }
        } catch (Exception e) {
            System.err.println("No se pudo abrir el PDF automáticamente: " + e.getMessage());
        }
    }
}
