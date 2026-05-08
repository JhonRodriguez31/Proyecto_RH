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

    private static final String DOWNLOADS_DIR = System.getProperty("user.home") + File.separator + "Downloads";
    private static final String BASE_DIR = DOWNLOADS_DIR + File.separator + "Guardar_Reportes";
    private static final String GENERAL_DIR = BASE_DIR + File.separator + "General";
    private static final String INDIVIDUAL_DIR = BASE_DIR + File.separator + "Individuales";

    public ReportServiceImpl() {
        createDirectories();
    }

    private void createDirectories() {
        String[] paths = {BASE_DIR, GENERAL_DIR, INDIVIDUAL_DIR};
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    @Override
    public void generarReporteEmpleados(List<Empleado> empleados) throws Exception {
        createDirectories(); // Ensure dirs exist
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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Lista_Empleados_" + timeStamp + ".pdf";
        String outputPath = GENERAL_DIR + File.separator + fileName;

        generatePdfFromHtml(htmlContent, outputPath);
        NotificacionService.exito("Reporte general generado en: " + outputPath);
        openPdf(outputPath);
    }

    @Override
    public void generarFichaEmpleado(Empleado empleado) throws Exception {
        createDirectories(); // Ensure dirs exist
        String template = loadTemplate("/com/project/templates/empleado_detail.html");

        String htmlContent = template
                .replace("{{FECHA}}", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()))
                .replace("{{CODIGO}}", empleado.getCodigoEmpleado() != null ? empleado.getCodigoEmpleado() : "")
                .replace("{{DNI}}", empleado.getDni() != null ? empleado.getDni() : "")
                .replace("{{NOMBRES}}", empleado.getNombres() != null ? empleado.getNombres() : "")
                .replace("{{APELLIDOS}}", empleado.getApellidos() != null ? empleado.getApellidos() : "")
                .replace("{{TELEFONO}}", empleado.getTelefono() != null ? empleado.getTelefono() : "");

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String dni = (empleado.getDni() != null ? empleado.getDni() : "SINDNI");
        String fileName = "Ficha_Empleado_" + dni + "_" + timeStamp + ".pdf";
        String outputPath = INDIVIDUAL_DIR + File.separator + fileName;

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
