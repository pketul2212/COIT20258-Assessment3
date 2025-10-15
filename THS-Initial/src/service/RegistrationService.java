package service;

public interface RegistrationService {
    String registerPatient(String fullName, String email, String password);

    /** Creates specialist profile + user account. Returns new specialistId (e.g., S001). */
    String registerSpecialist(String fullName, String email, String password, String specialty, String phone);
}
