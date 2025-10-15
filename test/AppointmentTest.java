import model.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class AppointmentTest {

    private Patient patient;
    private Specialist specialist;
    private LocalDateTime dateTime;
    private Appointment appointment;

    @Before
    public void setUp() {
        patient = new Patient("P1", "puser", "ppass", "Patient Name", "p@example.com", "123", "Addr", LocalDate.now());
        specialist = new Specialist("S1", "suser", "spass", "Specialist Name", "s@example.com", "456", "Cardiology", "LIC1");
        dateTime = LocalDateTime.of(2025, 10, 26, 10, 0);
        appointment = new Appointment(patient, specialist, dateTime, "Checkup");
    }

    @Test
    public void testConstructor_InitializesCorrectly() {
        assertEquals(patient, appointment.getPatient());
        assertEquals(specialist, appointment.getSpecialist());
        assertEquals(dateTime, appointment.getDateTime());
        assertEquals("Checkup", appointment.getReason());
        assertEquals(AppointmentStatus.BOOKED, appointment.getStatus());
    }

    @Test
    public void testReschedule_ChangesDateTimeAndStatus() {
        LocalDateTime newDateTime = LocalDateTime.of(2025, 10, 27, 11, 30);
        boolean result = appointment.reschedule(newDateTime);

        assertTrue("Reschedule should return true", result);
        assertEquals(newDateTime, appointment.getDateTime());
        assertEquals(AppointmentStatus.RESCHEDULED, appointment.getStatus());
    }

    @Test
    public void testCancel_UpdatesStatusAndNotes() {
        String cancelReason = "Patient unavailable";
        appointment.cancel(cancelReason);

        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
          assertEquals(cancelReason, appointment.getNotes());
    }

    @Test
    public void testComplete_UpdatesDiagnosisTreatmentAndStatus() {
        String diagnosis = "Common Cold";
        String treatment = "Rest and fluids";

        appointment.complete(diagnosis, treatment);

        assertEquals(diagnosis, appointment.getDiagnosis());
        assertEquals(treatment, appointment.getTreatment());
        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());
    }

     @Test
    public void testAddNotes_AppendsToNotes() {
        String initialNote = "Initial note";
        appointment.addNotes(initialNote);
        assertEquals(initialNote, appointment.getNotes());

        
        
    }
}