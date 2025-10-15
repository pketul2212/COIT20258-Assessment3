package repo.mysql;

import infra.Database;
import repo.AppointmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlAppointmentRepository implements AppointmentRepository {

    @Override public long countAll() { return scalarLong("SELECT COUNT(*) FROM appointments"); }
    @Override public long countForToday() { return scalarLong("SELECT COUNT(*) FROM appointments WHERE DATE(date_time)=CURRENT_DATE"); }
    @Override public long countForThisWeek() { return scalarLong("SELECT COUNT(*) FROM appointments WHERE YEARWEEK(date_time,1)=YEARWEEK(CURRENT_DATE,1)"); }
    @Override public long countForThisMonth() { return scalarLong("SELECT COUNT(*) FROM appointments WHERE YEAR(date_time)=YEAR(CURRENT_DATE) AND MONTH(date_time)=MONTH(CURRENT_DATE)"); }

    @Override
    public List<Row> findHistoryByPatientId(String patientId) {
        String sql = """
          SELECT CONCAT(p.first_name,' ',p.last_name) AS patientName,
                 CONCAT(s.first_name,' ',s.last_name) AS specialistName,
                 a.date_time, a.reason
          FROM appointments a
          JOIN patients p ON p.id=a.patient_id
          JOIN specialists s ON s.id=a.specialist_id
          WHERE a.patient_id=?
          ORDER BY a.date_time DESC
        """;
        List<Row> rows = new ArrayList<>();
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getString("patientName"),
                        rs.getString("specialistName"),
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        rs.getString("reason")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return rows;
    }

    private long scalarLong(String sql) {
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }
}
