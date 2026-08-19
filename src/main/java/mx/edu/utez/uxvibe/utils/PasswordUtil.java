package mx.edu.utez.uxvibe.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad para el manejo seguro de contraseñas.
 * Aplica SHA-256 sobre la contraseña. Soporta hashing con o sin salt.
 */
public class PasswordUtil {

    public static String generarSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password) {
        return hashPassword(password, "");
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            if (salt != null && !salt.isEmpty()) {
                md.update(Base64.getDecoder().decode(salt));
            }
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }

    public static boolean verificarPassword(String passwordIngresada, String salt, String hashGuardado) {
        return hashPassword(passwordIngresada, salt).equals(hashGuardado);
    }
}
