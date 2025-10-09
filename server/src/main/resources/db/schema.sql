
-- THS-Enhanced Schema
CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  pwd_hash VARCHAR(255) NOT NULL,
  role ENUM('PATIENT','SPECIALIST','STAFF') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
  patient_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR(100),
  dob DATE,
  contact VARCHAR(100),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS specialists (
  specialist_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR(100),
  specialty VARCHAR(100),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS appointments (
  appointment_id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  specialist_id INT NOT NULL,
  start_at DATETIME NOT NULL,
  status ENUM('BOOKED','RESCHEDULED','CANCELLED','COMPLETED') DEFAULT 'BOOKED',
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL,
  FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
  FOREIGN KEY (specialist_id) REFERENCES specialists(specialist_id)
);

CREATE TABLE IF NOT EXISTS vitals (
  vital_id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  recorded_at DATETIME NOT NULL,
  temp DECIMAL(4,1),
  hr INT,
  systolic INT,
  diastolic INT,
  FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

CREATE TABLE IF NOT EXISTS prescriptions (
  rx_id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  drug VARCHAR(100),
  dose VARCHAR(50),
  duration VARCHAR(50),
  status ENUM('ACTIVE','REFILL_REQUESTED','REFILLED') DEFAULT 'ACTIVE',
  issued_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL,
  notes_enc TEXT,
  FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- New Feature 1: Specialist availability slots
CREATE TABLE IF NOT EXISTS availability (
  slot_id INT AUTO_INCREMENT PRIMARY KEY,
  specialist_id INT NOT NULL,
  start_at DATETIME NOT NULL,
  end_at DATETIME NOT NULL,
  is_booked BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (specialist_id) REFERENCES specialists(specialist_id)
);

-- New Feature 2: Appointment reminders
CREATE TABLE IF NOT EXISTS reminders (
  reminder_id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_id INT NOT NULL,
  type ENUM('EMAIL','SMS','INAPP') DEFAULT 'INAPP',
  scheduled_at DATETIME NOT NULL,
  sent_at DATETIME NULL,
  FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

-- Audit log
CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  username VARCHAR(50),
  action VARCHAR(100),
  details TEXT
);
