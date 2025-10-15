package model;

import java.time.LocalDateTime;
import java.util.List;

public class HealthcareStaff extends UserAccount {
    private String staffId;
    private String department;
    private List<String> permissions;

    public HealthcareStaff(String id, String username, String passwordHash, String name, String email, String contact, String department) {
        super(id, username, passwordHash, name, email, contact, Role.HEALTHCARE_STAFF);
        this.staffId = id;
        this.department = department;
    }

    public boolean modifyAppointment(Appointment a, LocalDateTime newDateTime, String reason) {
        a.reschedule(newDateTime);
        a.addNotes(reason);
        return true;
    }

    public boolean cancelAppointment(Appointment a, String reason) {
        a.cancel(reason);
        return true;
    }

    @Override
    public String toString() {
        return "HealthcareStaff{" + name + ", dept=" + department + "}";
    }
}
