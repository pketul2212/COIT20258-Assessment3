package repo.memory;

import model.Appointment;
import model.DataStore;
import repo.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory adapter backed by DataStore.
 * Uses toString() for Patient/Specialist display names to avoid depending on model-specific getters.
 */
public class InMemoryAppointmentRepository implements AppointmentRepository {

    @Override
    public long countAll() {
        return DataStore.appointments.size();
    }

    @Override
    public long countForToday() {
        LocalDate today = LocalDate.now();
        return DataStore.appointments.stream()
                .filter(a -> a.getDateTime() != null && a.getDateTime().toLocalDate().equals(today))
                .count();
    }

    @Override
    public long countForThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate end = start.plusDays(6);
        return DataStore.appointments.stream()
                .filter(a -> {
                    if (a.getDateTime() == null) return false;
                    LocalDate d = a.getDateTime().toLocalDate();
                    return !d.isBefore(start) && !d.isAfter(end);
                })
                .count();
    }

    @Override
    public long countForThisMonth() {
        LocalDate today = LocalDate.now();
        return DataStore.appointments.stream()
                .filter(a -> {
                    if (a.getDateTime() == null) return false;
                    LocalDate d = a.getDateTime().toLocalDate();
                    return d.getYear() == today.getYear() && d.getMonth() == today.getMonth();
                })
                .count();
    }

    @Override
    public List<Row> findHistoryByPatientId(String patientId) {
        List<Row> rows = new ArrayList<>();
        for (Appointment a : DataStore.appointments) {
            if (a == null || a.getPatient() == null) continue;

            // Match target patient id (fallback: accept all if patientId is blank)
            boolean matches = (patientId == null || patientId.isBlank())
                    || patientId.equalsIgnoreCase(safeId(a));

            if (matches) {
                String pName = safePatientName(a);
                String sName = safeSpecialistName(a);
                rows.add(new Row(pName, sName, a.getDateTime(), a.getReason()));
            }
        }

        // If no data yet, provide demo rows so the UI shows something
        if (rows.isEmpty()) {
            rows = List.of(
                new Row("John Doe", "Dr Parth Patel", LocalDateTime.now().minusDays(2),  "Routine Check-up"),
                new Row("John Doe", "Dr Jane Doe",   LocalDateTime.now().minusDays(5),  "Follow-up Visit"),
                new Row("John Doe", "Dr Parth Patel", LocalDateTime.now().minusDays(12), "Flu Symptoms")
            );
        }
        return rows;
    }

    // ---- helpers ----

    private String safeId(Appointment a) {
        try {
            return a.getPatient().getId();
        } catch (Exception e) {
            return null;
        }
    }

    private String safePatientName(Appointment a) {
        try {
            // If your Patient has getName(), you can prefer it:
            // return a.getPatient().getName();
            return String.valueOf(a.getPatient()); // falls back to toString()
        } catch (Exception e) {
            return "Unknown Patient";
        }
    }

    private String safeSpecialistName(Appointment a) {
        try {
            if (a.getSpecialist() == null) return "N/A";
            // If your Specialist has getName(), you can prefer it:
            // return a.getSpecialist().getName();
            return String.valueOf(a.getSpecialist()); // falls back to toString()
        } catch (Exception e) {
            return "N/A";
        }
    }
}
