package repo.memory;

import model.Appointment;
import model.DataStore;
import repo.UserRepository;

import java.util.Objects;

/**
 * Counts unique patients/specialists from existing appointments.
 * Uses safe fallbacks so it doesn't depend on specific getters in your models.
 */
public class InMemoryUserRepository implements UserRepository {

    @Override
    public long countPatients() {
        return DataStore.appointments.stream()
                .map(Appointment::getPatient)
                .filter(Objects::nonNull)
                .map(p -> {
                    // prefer id if available; otherwise use toString()
                    try { return p.getId(); } catch (Exception e) { return p.toString(); }
                })
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    @Override
    public long countSpecialists() {
        return DataStore.appointments.stream()
                .map(Appointment::getSpecialist)
                .filter(Objects::nonNull)
                .map(s -> {
                    try { return s.getId(); } catch (Exception e) { return s.toString(); }
                })
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }
}
