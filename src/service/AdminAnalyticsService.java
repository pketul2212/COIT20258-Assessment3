package service;

public interface AdminAnalyticsService {
    long totalConsultations();
    long totalPatients();
    long totalSpecialists();

    long consultationsToday();
    long consultationsThisWeek();
    long consultationsThisMonth();
}
