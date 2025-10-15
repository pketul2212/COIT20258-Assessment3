package repo.mysql;

import infra.Database;
import repo.AuthRepository;

import java.sql.*;

public class MySqlAuthRepository implements AuthRepository {

    @Override
    public UserRow findByEmail(String email) {
        String sql = "SELECT id,email,password_hash,role,patient_id,specialist_id FROM user_accounts WHERE email=?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserRow(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("patient_id"),
                        rs.getString("specialist_id")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM user_accounts WHERE email=?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public void createPatientAccount(String patientId, String email, String passwordHash) {
        String sql = "INSERT INTO user_accounts(email,password_hash,role,patient_id) VALUES (?,?, 'PATIENT', ?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.setString(3, patientId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); throw new RuntimeException(e); }
    }

    @Override
    public void createAdminIfMissing(String email, String passwordHash) {
        if (emailExists(email)) return;
        String sql = "INSERT INTO user_accounts(email,password_hash,role) VALUES (?,?, 'ADMIN')";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
