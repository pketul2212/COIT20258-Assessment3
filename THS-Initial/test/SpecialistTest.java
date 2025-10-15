
import model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public class SpecialistTest {

    private Specialist specialist;
    private Patient patient;
    private Appointment appointment;

    @Before
    public void setUp() {
        specialist = new Specialist( "S1", "specialist", "12345", "Dr. Demo Specialist", "specialist@mail.com",
                "555-2222", "Cardiology", "LIC1001");
        patient = new Patient( "P1", "patient", "12345", "Patient", "patient@mail.com",
                "555-1111", "101 Main St", LocalDate.of(1985, 5, 15));
        appointment = new Appointment(patient, specialist, LocalDateTime.now(), "Test Appt");
        DataStore.appointments.add(appointment);
    }

    @Test
    public void testIssuePrescription_CreatesAndAddsToPatient() {
        String medication = "Aspirin";
        String dosage = "100mg";
        int days = 5;

        Prescription prescription = specialist.issuePrescription(patient, medication, dosage, days);

        assertNotNull("Prescription should not be null", prescription);
        assertEquals(medication, prescription.getMedication());
        assertEquals(dosage, prescription.getDosage());
        assertEquals(days, prescription.getDuration());
        assertEquals(PrescriptionStatus.ACTIVE, prescription.getStatus());
        assertTrue("Prescription should be in patient's list", patient.getPrescriptions().contains(prescription));
            }

    @Test
    public void testRecordDiagnosis_UpdatesAppointment() {
        String diagnosis = "Healthy";
        String treatment = "Vitamins";

        specialist.recordDiagnosis(appointment, diagnosis, treatment);

        assertEquals(diagnosis, appointment.getDiagnosis());
        assertEquals(treatment, appointment.getTreatment());
        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());
    }

    @Test
    public void testCreateClinicBooking_CreatesBooking() {
        String hospitalName = "City Hospital";
        LocalDateTime bookingDateTime = LocalDateTime.of(2025, 11, 1, 14, 0);
        String notes = "For MRI";

        ClinicBooking clinicBooking = specialist.createClinicBooking(patient, hospitalName, bookingDateTime, notes);

        assertNotNull("ClinicBooking should not be null", clinicBooking);
        assertEquals(patient.getPatientId(), clinicBooking.getPatientId()); 
        assertEquals(hospitalName, clinicBooking.getHospitalName()); 
        assertEquals(bookingDateTime, clinicBooking.getDateTime());
        assertEquals(notes, clinicBooking.getNotes()); 
        
    }

    @Test
    public void testReviewVitalSigns_ReturnsPatientVitals() {
        VitalSign v1 = patient.recordVitalSigns(37.0, 80, "120/80");
        VitalSign v2 = patient.recordVitalSigns(36.8, 78, "118/79");

        List<VitalSign> vitals = specialist.reviewVitalSigns(patient);

        assertNotNull("Vitals list should not be null", vitals);
        assertEquals(2, vitals.size());
        assertTrue("Should contain recorded vitals", vitals.contains(v1) && vitals.contains(v2));
    }
}