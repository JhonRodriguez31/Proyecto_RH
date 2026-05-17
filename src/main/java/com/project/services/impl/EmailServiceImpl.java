package com.project.services.impl;

import com.project.config.EnvConfig;
import com.project.services.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    @Override
    public void enviarCredenciales(String destinatario, String username, String passwordTemporal) {
        String host = EnvConfig.get("SMTP_HOST", "smtp.gmail.com");
        String port = EnvConfig.get("SMTP_PORT", "587");
        String user = EnvConfig.get("SMTP_USER");
        String pass = EnvConfig.get("SMTP_PASS");

        if (user == null || pass == null) {
            System.err.println("ERROR: SMTP_USER o SMTP_PASS no configurados en .env");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user, "PlanillaCore Admin"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Bienvenido a PlanillaCore - Tus Credenciales de Acceso");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #f9fafb; }
                        .header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #6366f1; }
                        .header h1 { color: #6366f1; margin: 0; }
                        .content { padding: 30px 0; }
                        .credentials { background-color: #ffffff; padding: 20px; border-radius: 8px; border: 1px dashed #6366f1; margin: 20px 0; }
                        .credential-item { margin: 10px 0; font-size: 16px; }
                        .label { font-weight: bold; color: #4b5563; }
                        .value { font-family: monospace; background: #f3f4f6; padding: 2px 6px; border-radius: 4px; color: #1f2937; }
                        .footer { text-align: center; font-size: 12px; color: #6b7280; border-top: 1px solid #e0e0e0; padding-top: 20px; }
                        .button { display: inline-block; padding: 12px 24px; background-color: #6366f1; color: #ffffff !important; text-decoration: none; border-radius: 6px; font-weight: bold; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>PlanillaCore</h1>
                        </div>
                        <div class="content">
                            <p>Hola,</p>
                            <p>¡Bienvenido al equipo! Se ha creado tu cuenta en el sistema de gestión de planillas. A continuación, encontrarás tus credenciales de acceso temporal:</p>
                            
                            <div class="credentials">
                                <div class="credential-item">
                                    <span class="label">Usuario (DNI):</span>
                                    <span class="value">%s</span>
                                </div>
                                <div class="credential-item">
                                    <span class="label">Contraseña Temporal:</span>
                                    <span class="value">%s</span>
                                </div>
                            </div>
                            
                            <p><strong>Nota:</strong> Por seguridad, el sistema te solicitará cambiar esta contraseña en tu primer inicio de sesión.</p>
                            
                            <center>
                                <a href="#" class="button">Acceder al Sistema</a>
                            </center>
                        </div>
                        <div class="footer">
                            <p>Este es un correo automático, por favor no respondas a este mensaje.</p>
                            <p>&copy; 2026 PlanillaCore System. Todos los derechos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(username, passwordTemporal);

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}
