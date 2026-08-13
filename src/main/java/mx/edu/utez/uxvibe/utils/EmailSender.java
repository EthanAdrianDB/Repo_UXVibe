package mx.edu.utez.uxvibe.utils;

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
                        .logo-badge { width: 42px; height: 42px; border: 3px solid #6c757d; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 20px; color: #6c757d; }
                        .brand-title { font-size: 26px; font-weight: 700; color: #111; }
                        .btn-reset { display: inline-block; background-color: #173E45; color: #ffffff !important; text-decoration: none; padding: 14px 28px; border-radius: 6px; font-weight: 600; font-size: 15px; margin: 25px 0; text-align: center; width: 80%%; }
                        .footer-note { font-size: 13px; color: #777; line-height: 1.5; margin-top: 25px; }
                        hr { border: none; border-top: 1px solid #e0e0e0; margin: 25px 0; }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
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
                        <p class="footer-note" style="color: #aaa; margin-top: 30px;">2026 UXVibe. Todos los derechos reservados.</p>
                    </div>
                </body>
                </html>
                """.formatted(enlaceRestablecer);
    }

    public static void sendMail(String destinatario, String asunto, String contenidoHtml) {
        // 1. Configuración del servidor SMTP (Actualizado para TLS moderno)
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true"); // Requerir TLS seguro obligatoriamente
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Solución al problema de TLS Handshake en Java moderno
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Timeouts para evitar congelamientos eternos si falla la red
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        // Variables temporales para la lógica
        String userTemp = System.getenv("SMTP_USER");
        String passTemp = System.getenv("SMTP_PASS");

        if (userTemp == null || passTemp == null) {
            System.err.println("Advertencia: Variables de entorno no encontradas. Buscando en credentials.properties...");
            java.util.Properties creds = new java.util.Properties();
            try (java.io.InputStream is = EmailSender.class.getClassLoader().getResourceAsStream("credentials.properties")) {
                if (is == null) {
                    throw new RuntimeException("No se encontró el archivo credentials.properties ni las variables de entorno.");
                }

                // Solución para respetar la codificación ISO-8859-1 del archivo
                try (java.io.InputStreamReader reader = new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.ISO_8859_1)) {
                    creds.load(reader);
                }

                userTemp = creds.getProperty("smtp.user");
                passTemp = creds.getProperty("smtp.pass");
            } catch (Exception e) {
                throw new RuntimeException("Error al cargar las credenciales: " + e.getMessage());
            }
        }

        // 2. Credenciales DEFINITIVAS y FINALES
        final String usuario = userTemp;
        final String contrasena = passTemp;

        // 3. Crear la sesión
        jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(usuario, contrasena);
            }
        });

        try {
            // 4. Crear el mensaje
            jakarta.mail.Message message = new jakarta.mail.internet.MimeMessage(session);
            message.setFrom(new jakarta.mail.internet.InternetAddress(usuario));
            message.setRecipients(jakarta.mail.Message.RecipientType.TO, jakarta.mail.internet.InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setContent(contenidoHtml, "text/html; charset=utf-8");

            // 5. Enviar
            jakarta.mail.Transport.send(message);
            System.out.println("¡Correo enviado con éxito a: " + destinatario + "!");

        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }
}
