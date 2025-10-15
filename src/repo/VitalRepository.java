package repo;

import java.time.LocalDateTime;
import java.util.List;

public interface VitalRepository {
    record Row(String patientId, String type, String value, LocalDateTime capturedAt) {}

    List<Row> findRecentByPatient(String patientId, int limit);
}
