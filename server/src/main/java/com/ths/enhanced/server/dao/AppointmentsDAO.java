
package com.ths.enhanced.server.dao;

import com.ths.enhanced.server.util.Db;
import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentsDAO {
    public static boolean isConflict(int specialistId, LocalDateTime startAt) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM appointments WHERE specialist_id=? AND start_at=? AND status <> 'CANCELLED'")) {
            ps.setInt(1, specialistId);
            ps.setTimestamp(2, Timestamp.valueOf(startAt));
            ResultSet rs = ps.executeQuery(); rs.next();
            return rs.getInt(1) > 0;
        }
    }
    public static int create(int patientId, int specialistId, LocalDateTime startAt, String notes) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("INSERT INTO appointments(patient_id,specialist_id,start_at,notes) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, patientId);
            ps.setInt(2, specialistId);
            ps.setTimestamp(3, Timestamp.valueOf(startAt));
            ps.setString(4, notes);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
            return -1;
        }
    }
    public static void reschedule(int apptId, LocalDateTime newStart) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("UPDATE appointments SET start_at=?, status='RESCHEDULED', updated_at=NOW() WHERE appointment_id=?")) {
            ps.setTimestamp(1, Timestamp.valueOf(newStart));
            ps.setInt(2, apptId);
            ps.executeUpdate();
        }
    }
    public static void cancel(int apptId) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("UPDATE appointments SET status='CANCELLED', updated_at=NOW() WHERE appointment_id=?")) {
            ps.setInt(1, apptId);
            ps.executeUpdate();
        }
    }
}
