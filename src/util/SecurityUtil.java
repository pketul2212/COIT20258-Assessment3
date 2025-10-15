package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Simple salted SHA-256 password hashing (no external libs). */
public final class SecurityUtil {
    private SecurityUtil() {}

    public static String hashPassword(String plain) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] digest = sha256(join(salt, plain.getBytes(StandardCharsets.UTF_8)));
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(digest);
    }

    public static boolean verifyPassword(String plain, String stored) {
        if (stored == null || !stored.contains(":")) return false;
        String[] parts = stored.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expected = Base64.getDecoder().decode(parts[1]);
        byte[] actual = sha256(join(salt, plain.getBytes(StandardCharsets.UTF_8)));
        if (actual.length != expected.length) return false;
        int diff = 0; for (int i=0;i<actual.length;i++) diff |= (actual[i] ^ expected[i]);
        return diff == 0;
    }

    private static byte[] sha256(byte[] data) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private static byte[] join(byte[] a, byte[] b) {
        byte[] out = new byte[a.length + b.length];
        System.arraycopy(a,0,out,0,a.length);
        System.arraycopy(b,0,out,a.length,b.length);
        return out;
    }
}
