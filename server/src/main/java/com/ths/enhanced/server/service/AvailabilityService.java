
package com.ths.enhanced.server.service;

import com.ths.enhanced.server.dao.AvailabilityDAO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AvailabilityService {
    public static Map<String,Object> list(Map<String,Object> body) {
        try {
            int specialistId = ((Double) body.get("specialistId")).intValue();
            List<Map<String,Object>> slots = AvailabilityDAO.list(specialistId);
            return Map.of("ok", true, "slots", slots);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "error", e.getMessage());
        }
    }
    public static Map<String,Object> add(Map<String,Object> body) {
        try {
            int specialistId = ((Double) body.get("specialistId")).intValue();
            LocalDateTime start = LocalDateTime.parse((String) body.get("startAt"));
            LocalDateTime end = LocalDateTime.parse((String) body.get("endAt"));
            int id = AvailabilityDAO.add(specialistId, start, end);
            return Map.of("ok", true, "slotId", id);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "error", e.getMessage());
        }
    }
}
