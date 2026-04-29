package com.project.config;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptFactory {
    private static final int BCRYPT_STRENGTH = 10;

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Contraseña no puede estar vacia ");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_STRENGTH));
    }


    public static boolean verifyPasswors(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static String generarContraseñaTemporal() {
        String caracteres = EnvConfig.get("PASSWORD_TEMPORAL");
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = (int) (Math.random() * caracteres.length());
            password.append(caracteres.charAt(index));
        }
        return password.toString();
    }

}
