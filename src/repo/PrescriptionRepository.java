package repo;

import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionRepository {
    record Row(String patientName, String medication, String dosage, LocalDateTime issuedAt, String status) {}

    List<Row> findByPatientId(String patientId);
}
