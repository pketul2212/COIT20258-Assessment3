package service.impl;

import repo.AppointmentRepository;
import repo.UserRepository;
import service.AdminAnalyticsService;

public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final AppointmentRepository apptRepo;
    private final UserRepository userRepo;

    public AdminAnalyticsServiceImpl(AppointmentRepository apptRepo, UserRepository userRepo) {
        this.apptRepo = apptRepo;
        this.userRepo = userRepo;
    }

    @Override public long totalConsultations()   { return apptRepo.countAll(); }
    @Override public long totalPatients()        { return userRepo.countPatients(); }
    @Override public long totalSpecialists()     { return userRepo.countSpecialists(); }
    @Override public long consultationsToday()   { return apptRepo.countForToday(); }
    @Override public long consultationsThisWeek(){ return apptRepo.countForThisWeek(); }
    @Override public long consultationsThisMonth(){ return apptRepo.countForThisMonth(); }
}
