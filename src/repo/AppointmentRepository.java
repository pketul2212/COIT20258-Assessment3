package repo;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {
    record Row(String patientName, String specialistName, LocalDateTime dateTime, String reason) {}

    long countAll();
    long countForToday();
    long countForThisWeek();
    long countForThisMonth();

    List<Row> findHistoryByPatientId(String patientId);
}
