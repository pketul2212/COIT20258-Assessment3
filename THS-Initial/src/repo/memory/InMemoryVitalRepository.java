package repo.memory;

import model.DataStore;
import repo.VitalRepository;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Reflection-safe adapter over DataStore.vitals.
 * Handles different model method names without changing VitalSign.
 */
public class InMemoryVitalRepository implements VitalRepository {

    @Override
    public List<Row> findRecentByPatient(String patientId, int limit) {
        return DataStore.vitals.stream()
                .map(v -> toRowIfMatches(v, patientId))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Row::capturedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(Math.max(1, limit))
                .collect(Collectors.toList());
    }

    // --------------- mapping helpers ---------------

    private Row toRowIfMatches(Object vital, String patientId) {
        String pid = extractPatientId(vital);
        if (patientId != null && !patientId.isBlank()) {
            if (pid == null || !patientId.equalsIgnoreCase(pid)) return null;
        }
        String type  = safeString(vital, "getType", "getVitalType", "type");
        String value = safeString(vital, "getValue", "getMeasurement", "value");
        LocalDateTime at = safeDateTime(vital, "getCapturedAt", "getDateTime", "getTimestamp", "getTime", "getDate");

        return new Row(pid, type, value, at);
    }

    private String extractPatientId(Object vital) {
        Object patient = call(vital, "getPatient");
        if (patient != null) {
            String id = safeString(patient, "getId");
            if (id != null && !id.isBlank()) return id;
        }
        String pid = safeString(vital, "getPatientId", "getPid");
        return (pid == null || pid.isBlank()) ? null : pid;
    }

    private static LocalDateTime safeDateTime(Object obj, String... methodNames) {
        Object v = call(obj, methodNames);
        if (v == null) return null;
        if (v instanceof LocalDateTime ldt) return ldt;
        if (v instanceof Date d) return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        if (v instanceof java.time.LocalDate ld) return ld.atStartOfDay();
        if (v instanceof CharSequence cs) {
            try { return LocalDateTime.parse(cs.toString()); } catch (Exception ignore) {}
        }
        return null;
    }

    private static String safeString(Object obj, String... methodNames) {
        Object v = call(obj, methodNames);
        return v == null ? null : String.valueOf(v);
    }

    private static Object call(Object target, String... methodNames) {
        if (target == null) return null;
        for (String m : methodNames) {
            try {
                Method mm = target.getClass().getMethod(m);
                mm.setAccessible(true);
                return mm.invoke(target);
            } catch (Exception ignore) {}
        }
        return null;
    }
}
