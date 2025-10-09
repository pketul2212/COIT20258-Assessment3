
package com.ths.enhanced.server.service;

import com.ths.enhanced.server.dao.UsersDAO;
import com.ths.enhanced.server.util.SecurityUtil;
import java.util.Map;

public class AuthService {
    public static Map<String,Object> login(Map<String,Object> body) {
        try {
            String username = (String) body.get("username");
            String password = (String) body.get("password");
            UsersDAO.Record r = UsersDAO.findByUsername(username);
            if (r == null) return Map.of("ok", false, "error", "User not found");
            if (!SecurityUtil.verify(password, r.pwdHash)) return Map.of("ok", false, "error", "Invalid password");
            return Map.of("ok", true, "userId", r.userId, "username", r.username, "role", r.role);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "error", e.getMessage());
        }
    }
}
