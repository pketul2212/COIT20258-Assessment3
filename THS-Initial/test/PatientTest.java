
import model.Patient;
import model.Appointment;
import model.VitalSign;
import model.Prescription;
import model.Specialist;
import model.AppointmentStatus;
import model.PrescriptionStatus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientTest {

    private Patient patient;
    private Specialist specialist;

    @Before
    public void setUp() {
        patient = new Patient(
                "P123", "testuser", "hashedPass123", "Test Patient",
                "test@example.com", "1234567890", "123 Test St", LocalDate.of(1990, 1, 1)
        );
        specialist = new Specialist("S1", "specuser", "pass123", "Dr. Spec",
                "spec@mail.com", "999", "Cardiology", "LIC123");

        System.out.println("DEBUG: Finished creatingpatient object");

    }

    @Test
    public void testBookAppointment_CreatesAndStoresAppointment() {
        LocalDateTime testDateTime = LocalDateTime.of(2025, 10, 20, 10, 30);
        String reason = "Routine Checkup";

        Appointment bookedAppointment = patient.bookAppointment(specialist, testDateTime, reason);

        assertNotNull("Booked appointment should not be null", bookedAppointment);
        assertEquals("Appointment patient should match", patient, bookedAppointment.getPatient());
        assertEquals("Appointment specialist should match", specialist, bookedAppointment.getSpecialist());
        assertEquals("Appointment dateTime should match", testDateTime, bookedAppointment.getDateTime());
        assertEquals("Appointment reason should match", reason, bookedAppointment.getReason());
        assertEquals("Status should be BOOKED", AppointmentStatus.BOOKED, bookedAppointment.getStatus());
    }

    @Test
    public void testRecordVitalSigns_CreatesAndStoresVitalSign() {
        double temperature = 36.8;
        int heartRate = 72;
        String bloodPressure = "120/80";

        VitalSign recordedVital = patient.recordVitalSigns(temperature, heartRate, bloodPressure);

        assertNotNull("Recorded vital sign should not be null", recordedVital);
        assertEquals(patient.getPatientId(), recordedVital.getPatientId());
        assertEquals(temperature, recordedVital.getTemperature(), 0.01);
        assertEquals(heartRate, recordedVital.getHeartRate());
        assertEquals(bloodPressure, recordedVital.getBloodPressure());
    }

    @Test
    public void testRequestRefill_ValidPrescription() {
        Prescription prescription = new Prescription(patient, specialist, "Ibuprofen", "200mg", 7);
        patient.getPrescriptions().add(prescription);

        assertEquals(PrescriptionStatus.ACTIVE, prescription.getStatus());

        boolean refillRequested = patient.requestRefill(prescription);

        assertTrue("Refill request should succeed", refillRequested);
        assertEquals(PrescriptionStatus.REFILL_REQUESTED, prescription.getStatus());
    }
}
