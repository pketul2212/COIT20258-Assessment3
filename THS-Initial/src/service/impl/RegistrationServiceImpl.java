package service.impl;

import repo.AuthRepository;
import repo.RegistrationRepository;
import repo.SpecialistRegistrationRepository;
import service.RegistrationService;
import util.SecurityUtil;

public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository regRepo;
    private final SpecialistRegistrationRepository specRepo;
    private final AuthRepository authRepo;

    public RegistrationServiceImpl(RegistrationRepository regRepo,
                                   SpecialistRegistrationRepository specRepo,
                                   AuthRepository authRepo) {
        this.regRepo = regRepo;
        this.specRepo = specRepo;
        this.authRepo = authRepo;
    }

    @Override
    public String registerPatient(String fullName, String email, String password) {
        if (authRepo.emailExists(email)) throw new IllegalStateException("Email already registered.");
        String patientId = regRepo.createPatient(fullName, email);
        String hash = SecurityUtil.hashPassword(password);
        authRepo.createPatientAccount(patientId, email, hash);
        return patientId;
    }

    @Override
    public String registerSpecialist(String fullName, String email, String password, String specialty, String phone) {
        if (authRepo.emailExists(email)) throw new IllegalStateException("Email already registered.");
        String specialistId = specRepo.createSpecialist(fullName, email, specialty, phone);
        String hash = SecurityUtil.hashPassword(password);
        specRepo.createSpecialistAccount(specialistId, email, hash);
        return specialistId;
    }
}
