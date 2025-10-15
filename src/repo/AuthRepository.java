package repo;

public interface AuthRepository {
    record UserRow(Long id, String email, String passwordHash, String role, String patientId, String specialistId) {}

    UserRow findByEmail(String email);
    boolean emailExists(String email);
    void createPatientAccount(String patientId, String email, String passwordHash);
    void createAdminIfMissing(String email, String passwordHash);
}
