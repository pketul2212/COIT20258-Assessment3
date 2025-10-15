package service.impl;

import repo.AppointmentRepository;
import repo.PrescriptionRepository;
import service.AppointmentHistoryService;

import java.util.List;
import java.util.stream.Collectors;

public class AppointmentHistoryServiceImpl implements AppointmentHistoryService {

    private final AppointmentRepository apptRepo;
    @SuppressWarnings("unused")
    private final PrescriptionRepository rxRepo;

    public AppointmentHistoryServiceImpl(AppointmentRepository apptRepo, PrescriptionRepository rxRepo) {
        this.apptRepo = apptRepo;
        this.rxRepo = rxRepo;
    }

    @Override
    public List<Row> historyForPatient(String patientId) {
        return apptRepo.findHistoryByPatientId(patientId).stream()
                .map(r -> new Row(r.patientName(), r.specialistName(), r.dateTime(), r.reason()))
                .collect(Collectors.toList());
    }
}
