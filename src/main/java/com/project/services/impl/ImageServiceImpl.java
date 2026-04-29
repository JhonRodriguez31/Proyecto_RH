package com.project.services.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.config.EnvConfig;
import com.project.services.ImageService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class ImageServiceImpl implements ImageService {
    private static final String BACKEND_URL = EnvConfig.get("BACKEND_URL");
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String subirImagen(File archivo) throws IOException, InterruptedException {
        if (!esImagenValida(archivo)) {
            throw new IllegalArgumentException("Archivo debe tener una extensión permitida");
        }

        String boundary = "----FormBoundary" + System.currentTimeMillis();
        byte[] body = construirMultipart(archivo, boundary);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();

        var response = httpClient.send(request,
                java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error al subir imagen: " + response.body());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        return json.get("url").getAsString();
    }

    private boolean esImagenValida(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".png") ||
                nombre.endsWith(".jpg") ||
                nombre.endsWith(".jpeg");
    }

    private byte[] construirMultipart(File archivo, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"archivo\"; filename=\"")
                .append(archivo.getName()).append("\"\r\n");

        String contentType = detectarTipo(archivo);
        sb.append("Content-Type: ").append(contentType).append("\r\n\r\n");

        byte[] header = sb.toString().getBytes();
        byte[] fileContent = java.nio.file.Files.readAllBytes(archivo.toPath());
        byte[] footer = ("\r\n--" + boundary + "--\r\n").getBytes();

        byte[] result = new byte[header.length + fileContent.length + footer.length];
        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(fileContent, 0, result, header.length, fileContent.length);
        System.arraycopy(footer, 0, result, header.length + fileContent.length, footer.length);

        return result;
    }

    private String detectarTipo(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        if (nombre.endsWith(".png"))
            return "image/png";
        if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg"))
            return "image/jpeg";
        return "application/octet-stream";
    }
}