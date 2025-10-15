package repo.mysql;

import infra.Database;
import repo.VitalRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlVitalRepository implements VitalRepository {

    @Override
    public List<Row> findRecentByPatient(String patientId, int limit) {
        String sql = """
          SELECT patient_id, type, value, captured_at
          FROM vitals
          WHERE patient_id=?
          ORDER BY captured_at DESC
          LIMIT ?
        """;
        List<Row> rows = new ArrayList<>();
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, patientId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getString("patient_id"),
                        rs.getString("type"),
                        rs.getString("value"),
                        rs.getTimestamp("captured_at").toLocalDateTime()
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return rows;
    }
}
