package repo.mysql;

import infra.Database;
import repo.SpecialistRegistrationRepository;

import java.sql.*;

public class MySqlSpecialistRegistrationRepository implements SpecialistRegistrationRepository {

    @Override
    public String createSpecialist(String fullName, String email, String specialty, String phone) {
        String id = nextSpecialistId();
        String[] parts = fullName.trim().split("\\s+", 2);
        String first = parts.length > 0 ? parts[0] : "";
        String last  = parts.length > 1 ? parts[1] : "";

        String sql = "INSERT INTO specialists (id, first_name, last_name, specialty, email, phone) VALUES (?,?,?,?,?,?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, specialty);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.executeUpdate();
            return id;
        } catch (Exception e) { e.printStackTrace(); throw new RuntimeException(e); }
    }

    @Override
    public void createSpecialistAccount(String specialistId, String email, String passwordHash) {
        String sql = "INSERT INTO user_accounts(email,password_hash,role,specialist_id) VALUES (?,?, 'SPECIALIST', ?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.setString(3, specialistId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); throw new RuntimeException(e); }
    }

    private String nextSpecialistId() {
        String sql = "SELECT id FROM specialists WHERE id LIKE 'S%' ORDER BY id DESC LIMIT 1";
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            int n = 0;
            if (rs.next()) {
                String last = rs.getString(1).substring(1);
                try { n = Integer.parseInt(last); } catch (Exception ignore) {}
            }
            return String.format("S%03d", n + 1);
        } catch (Exception e) { e.printStackTrace(); return "S001"; }
    }
}
