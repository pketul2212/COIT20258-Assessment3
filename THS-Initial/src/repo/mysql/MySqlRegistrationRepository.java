package repo.mysql;

import infra.Database;
import repo.RegistrationRepository;

import java.sql.*;

public class MySqlRegistrationRepository implements RegistrationRepository {

    @Override
    public String createPatient(String fullName, String email) {
        // Simple id generator P001..P999
        String nextId = nextPatientId();
        String[] parts = fullName.trim().split("\\s+", 2);
        String first = parts.length > 0 ? parts[0] : "";
        String last  = parts.length > 1 ? parts[1] : "";

        String sql = "INSERT INTO patients(id, first_name, last_name, email) VALUES (?,?,?,?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nextId);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, email);
            ps.executeUpdate();
            return nextId;
        } catch (Exception e) { e.printStackTrace(); throw new RuntimeException(e); }
    }

    private String nextPatientId() {
        String sql = "SELECT id FROM patients WHERE id LIKE 'P%' ORDER BY id DESC LIMIT 1";
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            int n = 0;
            if (rs.next()) {
                String last = rs.getString(1).substring(1);
                try { n = Integer.parseInt(last); } catch (Exception ignore) {}
            }
            return String.format("P%03d", n + 1);
        } catch (Exception e) { e.printStackTrace(); return "P001"; }
    }
}
