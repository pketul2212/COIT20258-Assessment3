package infra;

import java.sql.Connection;
import java.sql.Statement;

public final class SchemaInitializer {
    private SchemaInitializer() {}

    public static void ensureTables() {
        String patients = """
            CREATE TABLE IF NOT EXISTS patients (
              id VARCHAR(20) PRIMARY KEY,
              first_name VARCHAR(50), last_name VARCHAR(50),
              email VARCHAR(120) UNIQUE, phone VARCHAR(30),
              address VARCHAR(200), gender VARCHAR(10),
              date_of_birth DATE
            )
        """;
        String specialists = """
            CREATE TABLE IF NOT EXISTS specialists (
              id VARCHAR(20) PRIMARY KEY,
              first_name VARCHAR(50), last_name VARCHAR(50),
              specialty VARCHAR(80), email VARCHAR(120) UNIQUE, phone VARCHAR(30)
            )
        """;
        String appointments = """
            CREATE TABLE IF NOT EXISTS appointments (
              id BIGINT PRIMARY KEY AUTO_INCREMENT,
              patient_id VARCHAR(20) NOT NULL,
              specialist_id VARCHAR(20) NOT NULL,
              date_time DATETIME NOT NULL,
              reason VARCHAR(200), status VARCHAR(20),
              FOREIGN KEY (patient_id) REFERENCES patients(id),
              FOREIGN KEY (specialist_id) REFERENCES specialists(id),
              INDEX idx_appt_patient (patient_id),
              INDEX idx_appt_datetime (date_time)
            )
        """;
        String prescriptions = """
            CREATE TABLE IF NOT EXISTS prescriptions (
              id BIGINT PRIMARY KEY AUTO_INCREMENT,
              patient_id VARCHAR(20) NOT NULL,
              specialist_id VARCHAR(20) NOT NULL,
              medication VARCHAR(120),
              dosage VARCHAR(120),
              days INT,
              issued_at DATETIME,
              status VARCHAR(20),
              FOREIGN KEY (patient_id) REFERENCES patients(id),
              FOREIGN KEY (specialist_id) REFERENCES specialists(id),
              INDEX idx_rx_patient (patient_id),
              INDEX idx_rx_issued (issued_at)
            )
        """;
        String userAccounts = """
            CREATE TABLE IF NOT EXISTS user_accounts (
              id BIGINT PRIMARY KEY AUTO_INCREMENT,
              email VARCHAR(120) UNIQUE NOT NULL,
              password_hash VARCHAR(255) NOT NULL,
              role ENUM('ADMIN','PATIENT','SPECIALIST') NOT NULL,
              patient_id VARCHAR(20),
              specialist_id VARCHAR(20),
              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              FOREIGN KEY (patient_id) REFERENCES patients(id),
              FOREIGN KEY (specialist_id) REFERENCES specialists(id)
            )
        """;

        try (Connection c = Database.get();
             Statement st = c.createStatement()) {
            st.execute(patients);
            st.execute(specialists);
            st.execute(appointments);
            st.execute(prescriptions);
            st.execute(userAccounts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
