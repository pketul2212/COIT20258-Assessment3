

import model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class HealthcareStaffTest {

    private HealthcareStaff staff;
    private Patient patient;
    private Specialist specialist;
    private Appointment appointment;

    @Before
    public void setUp() {
        staff = new HealthcareStaff("H1", "huser", "hpass", "Staff Name", "h@example.com", "789", "Admin");
        patient = new Patient("P1", "puser", "ppass", "Jane Doe", "p@example.com", "123", "Addr", LocalDate.now());
        specialist = new Specialist("S1", "suser", "spass", "Dr. Jones", "s@example.com", "456", "General", "LIC2");
        appointment = new Appointment(patient, specialist, LocalDateTime.of(2025, 10, 28, 9, 0), "Follow-up");
        DataStore.appointments.add(appointment); 
    }

    @Test
    public void testModifyAppointment_ChangesDateTimeAndAddsNotes() {
        LocalDateTime newDateTime = LocalDateTime.of(2025, 10, 29, 10, 30);
        String reason = "Doctor's schedule";

        boolean result = staff.modifyAppointment(appointment, newDateTime, reason);

        assertTrue("Modify should return true", result);
        assertEquals(newDateTime, appointment.getDateTime());
        assertTrue(appointment.getNotes().contains(reason));
    }

    @Test
    public void testCancelAppointment_UpdatesStatusAndNotes() {
        String reason = "Duplicate booking";

        boolean result = staff.cancelAppointment(appointment, reason);

        assertTrue("Cancel should return true", result);
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertTrue(appointment.getNotes().contains(reason));
    }
}