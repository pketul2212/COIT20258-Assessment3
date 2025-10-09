
package com.ths.enhanced.server.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtil {
    public static String hash(String password) throws Exception {
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
        byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        return "$PBKDF2$10000$" + Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
    }
    public static boolean verify(String password, String stored) throws Exception {
        String[] parts = stored.split("\$");
        int iterations = Integer.parseInt(parts[2]);
        byte[] salt = Base64.getDecoder().decode(parts[3]);
        byte[] expected = Base64.getDecoder().decode(parts[4]);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, expected.length*8);
        byte[] actual = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        if (actual.length != expected.length) return false;
        int diff = 0; for (int i=0;i<actual.length;i++) diff |= actual[i]^expected[i];
        return diff==0;
    }
}
