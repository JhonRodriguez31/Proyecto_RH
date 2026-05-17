package com.project.services;

public interface EmailService {
    void enviarCredenciales(String destinatario, String username, String passwordTemporal);
}
