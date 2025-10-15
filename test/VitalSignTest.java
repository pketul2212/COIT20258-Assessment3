import model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VitalSignTest {

    private String patientId;
    private double temperature;
    private int heartRate;
    private String bloodPressure;

    @Before
    public void setUp() {
        patientId = "P123";
        temperature = 37.0;
        heartRate = 75;
        bloodPressure = "120/80";
    }

    @Test
    public void testConstructor_InitializesCorrectly() {
        VitalSign vitalSign = new VitalSign(patientId, temperature, heartRate, bloodPressure);

        assertNotNull("Timestamp should be set", vitalSign.getTimestamp());
        assertEquals(patientId, vitalSign.getPatientId());
        assertEquals(temperature, vitalSign.getTemperature(), 0.01);
        assertEquals(heartRate, vitalSign.getHeartRate());
        assertEquals(bloodPressure, vitalSign.getBloodPressure());
        
    }

    @Test
    public void testIsAbnormal_DetectsAbnormal() {
        VitalSign abnormalTemp = new VitalSign(patientId, 40.0, 75, "120/80");
        assertTrue("High temperature should be abnormal", abnormalTemp.isAbnormal());

        VitalSign abnormalHR = new VitalSign(patientId, 37.0, 150, "120/80");
        assertTrue("High heart rate should be abnormal", abnormalHR.isAbnormal());

        VitalSign normal = new VitalSign(patientId, 36.5, 70, "110/70");
        assertFalse("Normal values should not be abnormal", normal.isAbnormal());
    }

  
}