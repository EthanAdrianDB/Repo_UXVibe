package mx.edu.utez.uxvibe.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad para encriptar y verificar contraseñas.
 * Usamos el algoritmo SHA-256 para transformar la contraseña en texto plano
 * a un hash seguro en Base64, evitando que se guarden contraseñas directas en la BD.
 */
public class PasswordUtil {

    /**
     * Genera una cadena aleatoria criptográficamente segura (Salt)
     * de 16 bytes convertida a Base64 para añadir más seguridad al hash.
     */
    public static String generarSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashea una contraseña usando SHA-256 sin salt (versión directa).
     * @param password Contraseña que escribió el usuario.
     * @return Cadena con el hash en formato Base64.
     */
    public static String hashPassword(String password) {
        return hashPassword(password, "");
    }

    /**
     * Genera el hash SHA-256 combinando la contraseña con un Salt opcional.
     * Convierte los bytes resultantes en una cadena legible con Base64.
     */
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

    /**
     * Compara la contraseña que acaba de escribir el usuario contra el hash guardado en la base de datos.
     * @return true si coinciden, false si no es correcta.
     */
    public static boolean verificarPassword(String passwordIngresada, String salt, String hashGuardado) {
        return hashPassword(passwordIngresada, salt).equals(hashGuardado);
    }
}
