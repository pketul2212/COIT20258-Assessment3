package service.impl;

import repo.AuthRepository;
import service.AuthService;
import util.SecurityUtil;
import util.Session;

public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepo;

    public AuthServiceImpl(AuthRepository authRepo) {
        this.authRepo = authRepo;
    }

    @Override
    public boolean login(String email, String password) {
        var user = authRepo.findByEmail(email);
        if (user == null) return false;
        if (!SecurityUtil.verifyPassword(password, user.passwordHash())) return false;

        // fill session
        Session.currentEmail = user.email();
        Session.currentRole  = user.role();
        Session.currentPatientId    = user.patientId();
        Session.currentSpecialistId = user.specialistId();
        return true;
        }

    @Override
    public void ensureDefaultAdmin(String email, String password) {
        String hash = SecurityUtil.hashPassword(password);
        authRepo.createAdminIfMissing(email, hash);
    }
}
