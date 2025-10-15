package util;

public final class Session {
    private Session() {}
    public static String currentEmail;
    public static String currentRole;         // "ADMIN" | "PATIENT" | "SPECIALIST"
    public static String currentPatientId;    // set for PATIENT
    public static String currentSpecialistId; // set for SPECIALIST

    public static void clear() {
        currentEmail = null;
        currentRole = null;
        currentPatientId = null;
        currentSpecialistId = null;
    }
}
