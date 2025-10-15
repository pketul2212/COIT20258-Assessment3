package repo.mysql;

import infra.Database;
import repo.UserRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlUserRepository implements UserRepository {

    @Override
    public long countPatients() {
        return scalar("SELECT COUNT(*) FROM patients");
    }

    @Override
    public long countSpecialists() {
        return scalar("SELECT COUNT(*) FROM specialists");
    }

    private long scalar(String sql) {
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }
}
