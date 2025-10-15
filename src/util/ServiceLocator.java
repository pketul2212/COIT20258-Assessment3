package util;

import repo.*;
import repo.mysql.*;
import service.*;
import service.impl.*;

public final class ServiceLocator {
    private ServiceLocator() {}

    private static final boolean USE_MYSQL = true;

    private static AppointmentRepository appointmentRepo;
    private static PrescriptionRepository prescriptionRepo;
    private static UserRepository userRepo;
    private static VitalRepository vitalRepo;
    private static AuthRepository authRepo;
    private static RegistrationRepository registrationRepo;
    private static SpecialistRegistrationRepository specialistRegRepo;

    private static AdminAnalyticsService adminAnalyticsService;
    private static AppointmentHistoryService appointmentHistoryService;
    private static AuthService authService;
    private static RegistrationService registrationService;

    private static void initRepos() {
        if (appointmentRepo != null) return;
        if (USE_MYSQL) {
            appointmentRepo      = new MySqlAppointmentRepository();
            prescriptionRepo     = new MySqlPrescriptionRepository();
            userRepo             = new MySqlUserRepository();
            vitalRepo            = new MySqlVitalRepository();
            authRepo             = new MySqlAuthRepository();
            registrationRepo     = new MySqlRegistrationRepository();
            specialistRegRepo    = new MySqlSpecialistRegistrationRepository();
        } else {
            // in-memory fallbacks if ever needed
            appointmentRepo      = new repo.memory.InMemoryAppointmentRepository();
            prescriptionRepo     = new repo.memory.InMemoryPrescriptionRepository();
            userRepo             = new repo.memory.InMemoryUserRepository();
            vitalRepo            = new repo.memory.InMemoryVitalRepository();
            authRepo             = null;
            registrationRepo     = null;
            specialistRegRepo    = null;
        }
    }

    public static AdminAnalyticsService adminAnalytics() {
        if (adminAnalyticsService == null) { initRepos();
            adminAnalyticsService = new AdminAnalyticsServiceImpl(appointmentRepo, userRepo); }
        return adminAnalyticsService;
    }

    public static AppointmentHistoryService appointmentHistory() {
        if (appointmentHistoryService == null) { initRepos();
            appointmentHistoryService = new AppointmentHistoryServiceImpl(appointmentRepo, prescriptionRepo); }
        return appointmentHistoryService;
    }

    public static AuthService auth() {
        if (authService == null) { initRepos(); authService = new AuthServiceImpl(authRepo); }
        return authService;
    }

    public static RegistrationService registration() {
        if (registrationService == null) { initRepos();
            registrationService = new RegistrationServiceImpl(registrationRepo, specialistRegRepo, authRepo); }
        return registrationService;
    }
}
