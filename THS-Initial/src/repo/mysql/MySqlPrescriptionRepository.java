package repo.mysql;

import infra.Database;
import repo.PrescriptionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlPrescriptionRepository implements PrescriptionRepository {

    @Override
    public List<Row> findByPatientId(String patientId) {
        String sql = """
          SELECT CONCAT(p.first_name,' ',p.last_name) AS patientName,
                 pr.medication, pr.dosage, pr.issued_at, pr.status
          FROM prescriptions pr
          JOIN patients p ON p.id=pr.patient_id
          WHERE pr.patient_id=?
          ORDER BY pr.issued_at DESC
        """;
        List<Row> rows = new ArrayList<>();
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getString("patientName"),
                        rs.getString("medication"),
                        rs.getString("dosage"),
                        rs.getTimestamp("issued_at").toLocalDateTime(),
                        rs.getString("status")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return rows;
    }
}
