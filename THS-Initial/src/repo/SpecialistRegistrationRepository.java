package repo;

public interface SpecialistRegistrationRepository {
    /** Create specialist profile (returns new id like S001). */
    String createSpecialist(String fullName, String email, String specialty, String phone);

    /** Create linked user account with role SPECIALIST. */
    void createSpecialistAccount(String specialistId, String email, String passwordHash);
}
