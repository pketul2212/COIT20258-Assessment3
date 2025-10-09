
package com.ths.enhanced.server.service;

import com.ths.enhanced.server.dao.AppointmentsDAO;
import com.ths.enhanced.server.dao.AvailabilityDAO;
import com.ths.enhanced.server.dao.RemindersDAO;
import java.time.LocalDateTime;
import java.util.Map;

public class BookingService {
    public static Map<String,Object> book(Map<String,Object> body) {
        try {
            int patientId = ((Double) body.get("patientId")).intValue();
            int specialistId = ((Double) body.get("specialistId")).intValue();
            String start = (String) body.get("startAt");
            String notes = (String) body.getOrDefault("notes", "");
            LocalDateTime startAt = LocalDateTime.parse(start);

            if (AppointmentsDAO.isConflict(specialistId, startAt))
                return Map.of("ok", false, "error", "Time slot conflict");
            int apptId = AppointmentsDAO.create(patientId, specialistId, startAt, notes);

            // Reminder 24h before
            RemindersDAO.schedule(apptId, startAt.minusHours(24));
            return Map.of("ok", true, "appointmentId", apptId);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "error", e.getMessage());
        }
    }
    public static Map<String,Object> reschedule(Map<String,Object> body) {
        try {
            int apptId = ((Double) body.get("appointmentId")).intValue();
            LocalDateTime newStart = LocalDateTime.parse((String) body.get("newStartAt"));
            AppointmentsDAO.reschedule(apptId, newStart);
            RemindersDAO.schedule(apptId, newStart.minusHours(24));
            return Map.of("ok", true);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "error", e.getMessage());
        }
    }
    public static Map<String,Object> cancel(Map<String,Object> body) {
        try {
            int apptId = ((Double) body.get("appointmentId")).intValue();
            AppointmentsDAO.cancel(apptId);
            return Map.of("ok", true);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "error", e.getMessage());
        }
    }
}
