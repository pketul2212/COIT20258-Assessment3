package service;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentHistoryService {
    record Row(String patientName, String specialistName, LocalDateTime dateTime, String reason) {}

    List<Row> historyForPatient(String patientId);
}
