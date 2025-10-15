package repo;

public interface RegistrationRepository {
    /**
     * Creates a Patient with generated id (like 'P001') and returns that id.
     * Only minimal fields needed for login flow (name + email).
     */
    String createPatient(String fullName, String email);
}
