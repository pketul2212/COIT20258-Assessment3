package model;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static List<Appointment> appointments = new ArrayList<>();
    public static List<Prescription> prescriptions = new ArrayList<>();
    public static List<VitalSign> vitals = new ArrayList<>();

    public static UserAccount currentUser;
}
