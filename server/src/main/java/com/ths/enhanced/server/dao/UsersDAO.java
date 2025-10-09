
package com.ths.enhanced.server.dao;

import com.ths.enhanced.server.util.Db;
import java.sql.*;

public class UsersDAO {
    public static Record findByUsername(String username) throws Exception {
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement("SELECT user_id, username, pwd_hash, role FROM users WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Record(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }
            return null;
        }
    }
    public static class Record {
        public final int userId; public final String username; public final String pwdHash; public final String role;
        public Record(int id, String u, String p, String r){ this.userId=id; this.username=u; this.pwdHash=p; this.role=r;}
    }
}
