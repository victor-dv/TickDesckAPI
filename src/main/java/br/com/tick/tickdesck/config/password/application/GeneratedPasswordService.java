package br.com.tick.tickdesck.config.password.application;

import java.security.SecureRandom;
import java.util.Base64;

public class GeneratedPasswordService {
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }
}
