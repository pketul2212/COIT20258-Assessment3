
package com.ths.enhanced.server.service;

import com.ths.enhanced.server.dao.RemindersDAO;
import java.util.List;
import java.util.Map;

public class ReminderService {
    public static Map<String,Object> list(Map<String,Object> body) {
        try {
            int patientId = ((Double) body.get("patientId")).intValue();
            List<Map<String,Object>> rs = RemindersDAO.listByPatient(patientId);
            return Map.of("ok", true, "reminders", rs);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "error", e.getMessage());
        }
    }
}
