
package com.ths.enhanced.server.dao;

import com.ths.enhanced.server.util.Db;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemindersDAO {
    public static int schedule(int apptId, LocalDateTime when) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("INSERT INTO reminders(appointment_id, scheduled_at) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, apptId);
            ps.setTimestamp(2, Timestamp.valueOf(when));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        }
    }
    public static List<Map<String,Object>> listByPatient(int patientId) throws Exception {
        String sql = "SELECT r.reminder_id, r.scheduled_at, r.sent_at, a.appointment_id FROM reminders r JOIN appointments a ON r.appointment_id=a.appointment_id WHERE a.patient_id=? ORDER BY r.scheduled_at DESC";
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(Map.of(
                        "reminderId", rs.getInt(1),
                        "scheduledAt", rs.getTimestamp(2).toLocalDateTime().toString(),
                        "sentAt", rs.getTimestamp(3) == null ? null : rs.getTimestamp(3).toLocalDateTime().toString(),
                        "appointmentId", rs.getInt(4)
                ));
            }
        }
        return out;
    }
}
