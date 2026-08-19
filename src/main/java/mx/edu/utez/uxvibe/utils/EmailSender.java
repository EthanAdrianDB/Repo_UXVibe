package mx.edu.utez.uxvibe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailSender {

    public static String generarPlantillaRecuperacion(String enlaceRestablecer) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; margin:0; padding:40px 20px; background-color: #f9f9f9; }
                        .email-container { max-width: 550px; background: #ffffff; padding: 40px; margin: 0 auto; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
                        .header { display: flex; align-items: center; gap: 12px; margin-bottom: 30px; }
                        .brand-logo-img { width: 80px; height: 80px; object-fit: contain; }
                        .brand-title { font-size: 26px; font-weight: 700; color: #173E45; }
                        .btn-reset { display: inline-block; background-color: #173E45; color: #ffffff !important; text-decoration: none; padding: 14px 28px; border-radius: 6px; font-weight: 600; font-size: 15px; margin: 25px 0; text-align: center; }
                        .footer-note { font-size: 13px; color: #777; line-height: 1.5; margin-top: 25px; }
                        hr { border: none; border-top: 1px solid #e0e0e0; margin: 25px 0; }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            <img src="https://raw.githubusercontent.com/EthanAdrianDB/Repo_UXVibe/main/src/main/webapp/assets/images/logoux.png" alt="UXVibe Logo" class="brand-logo-img">
                            <span class="brand-title">UXVibe</span>
                        </div>
                        <p>Hola,</p>
                        <p>Recibimos una solicitud para restablecer la contraseña de tu cuenta en UXVibe.</p>
                        <p>Para crear una nueva contraseña, haz clic en el siguiente botón:</p>
                        <div style="text-align: center;">
                            <a href="%s" class="btn-reset">Restablecer mi contraseña</a>
                        </div>
                        <p class="footer-note">Este enlace expirará en 60 minutos por seguridad.</p>
                        <hr>
                        <p><strong>Si no fuiste tú:</strong></p>
                        <p class="footer-note">Si no solicitaste este cambio, puedes ignorar este correo. Tu contraseña actual seguirá siendo válida.</p>
                        <p class="footer-note" style="color: #aaa; margin-top: 30px;">&copy;2026 UXVibe | Todos los derechos reservados.</p>
                    </div>
                </body>
                </html>
                """.formatted(enlaceRestablecer);
    }

    private static Properties cargarCredenciales() {
        Properties props = new Properties();

        // 1. Intentar desde variables de entorno
        String envUser = System.getenv("SMTP_USER");
        String envPass = System.getenv("SMTP_PASS");
        if (envUser != null && !envUser.trim().isEmpty() && envPass != null && !envPass.trim().isEmpty()) {
            props.setProperty("smtp.user", envUser.trim());
            props.setProperty("smtp.pass", envPass.trim());
            return props;
        }

        // 2. Intentar desde ClassLoader
        ClassLoader[] loaders = new ClassLoader[]{
                Thread.currentThread().getContextClassLoader(),
                EmailSender.class.getClassLoader(),
                ClassLoader.getSystemClassLoader()
        };

        for (ClassLoader cl : loaders) {
            if (cl != null) {
                try (InputStream is = cl.getResourceAsStream("credentials.properties")) {
                    if (is != null) {
                        try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                            props.load(reader);
                            if (props.getProperty("smtp.user") != null && !props.getProperty("smtp.user").trim().isEmpty()) {
                                return props;
                            }
                        }
                    }
                } catch (Exception ignored) {}
            }
        }

        // 3. Fallback a rutas de disco
        File[] candidateFiles = new File[]{
                new File("src/main/resources/credentials.properties"),
                new File("C:/Users/artur/Desktop/Repo_UXVibe/src/main/resources/credentials.properties"),
                new File("../src/main/resources/credentials.properties"),
                new File("../../src/main/resources/credentials.properties")
        };

        for (File file : candidateFiles) {
            if (file.exists() && file.isFile()) {
                try (InputStream is = new FileInputStream(file);
                     InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    props.load(reader);
                    if (props.getProperty("smtp.user") != null && !props.getProperty("smtp.user").trim().isEmpty()) {
                        return props;
                    }
                } catch (Exception ignored) {}
            }
        }

        return props;
    }

    public static void sendMail(String destinatario, String asunto, String contenidoHtml) {
        Properties creds = cargarCredenciales();
        String usuario = creds.getProperty("smtp.user");
        String contrasena = creds.getProperty("smtp.pass");

        if (usuario == null || usuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalStateException("Las credenciales de correo (smtp.user / smtp.pass) no están configuradas en credentials.properties ni en las variables de entorno.");
        }

        usuario = usuario.trim();
        contrasena = contrasena.trim().replace(" ", ""); // Las contraseñas de app de Google a veces llevan espacios

        // 1. Configuración SMTP para Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        final String finalUser = usuario;
        final String finalPass = contrasena;

        jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(finalUser, finalPass);
            }
        });

        try {
            jakarta.mail.Message message = new jakarta.mail.internet.MimeMessage(session);
            message.setFrom(new jakarta.mail.internet.InternetAddress(finalUser, "UXVibe"));
            message.setRecipients(jakarta.mail.Message.RecipientType.TO, jakarta.mail.internet.InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setContent(contenidoHtml, "text/html; charset=utf-8");

            jakarta.mail.Transport.send(message);
            System.out.println("[EmailSender] Correo enviado exitosamente a: " + destinatario);

        } catch (Exception e) {
            System.err.println("[EmailSender] Error al enviar correo a " + destinatario + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}

