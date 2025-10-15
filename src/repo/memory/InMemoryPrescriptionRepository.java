package repo.memory;

import model.DataStore;
import repo.PrescriptionRepository;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reflection-safe adapter over DataStore.prescriptions.
 * Works with various possible model method names without changing your models.
 */
public class InMemoryPrescriptionRepository implements PrescriptionRepository {

    @Override
    public List<Row> findByPatientId(String patientId) {
        List<Row> rows = new ArrayList<>();

        for (Object p : DataStore.prescriptions) {
            // match patient
            String pid = safePatientId(p);
            if (patientId != null && !patientId.isBlank()) {
                if (pid == null || !patientId.equalsIgnoreCase(pid)) continue;
            }

            String patientName = safePatientName(p);
            String medication  = safeString(p, "getMedication");
            String dosage      = safeString(p, "getDosage");
            LocalDateTime issuedAt = safeDateTime(p, "getIssuedAt", "getDateTime", "getIssuedOn", "getTime", "getDate");
            String status      = safeStatus(p);

            rows.add(new Row(patientName, medication, dosage, issuedAt, status));
        }

        return rows;
    }

    // ---------------- helpers ----------------

    private static String safePatientId(Object rx) {
        Object patient = call(rx, "getPatient"); // may be Patient
        if (patient != null) {
            String id = safeString(patient, "getId");
            if (id != null) return id;
        }
        String pid = safeString(rx, "getPatientId", "getPid");
        return (pid == null || pid.isBlank()) ? null : pid;
    }

    private static String safePatientName(Object rx) {
        Object patient = call(rx, "getPatient");
        if (patient != null) {
            String full = safeString(patient, "getName");
            if (full != null && !full.isBlank()) return full;

            String fn = safeString(patient, "getFirstName", "getGivenName");
            String ln = safeString(patient, "getLastName", "getFamilyName", "getSurname");
            if (fn != null || ln != null) {
                return (nvl(fn) + " " + nvl(ln)).trim();
            }
            return patient.toString();
        }
        return "Unknown Patient";
    }

    private static String safeStatus(Object rx) {
        Object st = call(rx, "getStatus");
        if (st == null) return "UNKNOWN";
        if (st instanceof Enum<?> e) return e.name();
        return st.toString();
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

    private static String nvl(String s) { return s == null ? "" : s; }
}
