
package com.ths.enhanced.server.dao;

import com.ths.enhanced.server.util.Db;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvailabilityDAO {
    public static List<Map<String,Object>> list(int specialistId) throws Exception {
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("SELECT slot_id,start_at,end_at,is_booked FROM availability WHERE specialist_id=? ORDER BY start_at")) {
            ps.setInt(1, specialistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(Map.of(
                        "slotId", rs.getInt(1),
                        "startAt", rs.getTimestamp(2).toLocalDateTime().toString(),
                        "endAt", rs.getTimestamp(3).toLocalDateTime().toString(),
                        "booked", rs.getBoolean(4)
                ));
            }
        }
        return out;
    }
    public static int add(int specialistId, LocalDateTime startAt, LocalDateTime endAt) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("INSERT INTO availability(specialist_id,start_at,end_at) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, specialistId);
            ps.setTimestamp(2, Timestamp.valueOf(startAt));
            ps.setTimestamp(3, Timestamp.valueOf(endAt));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        }
    }
    public static void markBooked(int slotId) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("UPDATE availability SET is_booked=TRUE WHERE slot_id=?")) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        }
    }
}
