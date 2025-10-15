package service;

public interface AuthService {
    /** Returns true if login ok; fills Session (email/role/patientId/specialistId). */
    boolean login(String email, String password);

    /** Ensures a default admin exists (for demo/admin access). */
    void ensureDefaultAdmin(String email, String password);
}
