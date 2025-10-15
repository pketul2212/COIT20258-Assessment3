
-- Telehealth System (THS) - Schema & Seed Data
-- Generated: 2025-10-15T08:41:50
-- MySQL 8.x  |  Charset: utf8mb4  |  Engine: InnoDB

-- 1) Create database and user (optional - run with admin privileges)
CREATE DATABASE IF NOT EXISTS ths CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'ths_user'@'localhost' IDENTIFIED BY 'ths_pass';

GRANT ALL PRIVILEGES ON ths.* TO 'ths_user'@'localhost';
FLUSH PRIVILEGES;

USE ths;

-- 2) Tables (drop order handles FKs)
DROP TABLE IF EXISTS prescriptions;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS vitals;
DROP TABLE IF EXISTS user_accounts;
DROP TABLE IF EXISTS specialists;
DROP TABLE IF EXISTS patients;

CREATE TABLE patients (
  id VARCHAR(20) PRIMARY KEY,
  first_name VARCHAR(50),
  last_name  VARCHAR(50),
  email      VARCHAR(120) UNIQUE,
  phone      VARCHAR(30),
  address    VARCHAR(200),
  gender     VARCHAR(10),
  date_of_birth DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE specialists (
  id VARCHAR(20) PRIMARY KEY,
  first_name VARCHAR(50),
  last_name  VARCHAR(50),
  specialty  VARCHAR(80),
  email      VARCHAR(120) UNIQUE,
  phone      VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE appointments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id    VARCHAR(20) NOT NULL,
  specialist_id VARCHAR(20) NOT NULL,
  date_time     DATETIME NOT NULL,
  reason        VARCHAR(200),
  status        VARCHAR(20),
  CONSTRAINT fk_appt_patient    FOREIGN KEY (patient_id)    REFERENCES patients(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_appt_specialist FOREIGN KEY (specialist_id) REFERENCES specialists(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX idx_appt_patient (patient_id),
  INDEX idx_appt_specialist (specialist_id),
  INDEX idx_appt_datetime (date_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE prescriptions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  appointment_id BIGINT,
  patient_id     VARCHAR(20),
  specialist_id  VARCHAR(20),
  medication     VARCHAR(120),
  dosage         VARCHAR(120),
  days           INT,
  issued_at      DATETIME,
  status         VARCHAR(20),
  CONSTRAINT fk_rx_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_rx_patient     FOREIGN KEY (patient_id)     REFERENCES patients(id)     ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_rx_specialist  FOREIGN KEY (specialist_id)  REFERENCES specialists(id)  ON DELETE SET NULL ON UPDATE CASCADE,
  INDEX idx_rx_patient (patient_id),
  INDEX idx_rx_issued (issued_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE vitals (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id  VARCHAR(20) NOT NULL,
  type        VARCHAR(50),
  value       VARCHAR(50),
  captured_at DATETIME,
  CONSTRAINT fk_vital_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE ON UPDATE CASCADE,
  INDEX idx_vital_patient (patient_id),
  INDEX idx_vital_captured (captured_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_accounts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email         VARCHAR(120) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role          ENUM('ADMIN','PATIENT','SPECIALIST') NOT NULL,
  patient_id    VARCHAR(20),
  specialist_id VARCHAR(20),
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_user_patient    FOREIGN KEY (patient_id)    REFERENCES patients(id)    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_user_specialist FOREIGN KEY (specialist_id) REFERENCES specialists(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) Seed data
INSERT INTO patients (id, first_name, last_name, email, phone) VALUES
('P001','John','Doe','john@example.com','9000000001')
ON DUPLICATE KEY UPDATE email=VALUES(email);

INSERT INTO specialists (id, first_name, last_name, specialty, email, phone) VALUES
('S001','Jane','Smith','Dermatology','drjane@example.com','9000000002')
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Admin account (admin123)
INSERT INTO user_accounts (email, password_hash, role)
VALUES ('admin@ths.com', 'CX6anO37s5HA0KJjIGLcSw==:I0M85a9/zrU9GQ+OD9RDmf4nDMmHR2sFZDvWAcFiAEc=', 'ADMIN')
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Patient user mapped to P001 (john123)
INSERT INTO user_accounts (email, password_hash, role, patient_id)
VALUES ('john@example.com', '81Rb2611AtngijpgdkhtpA==:lWMqacTN5icnqOgZ12RSl3LVM1H8fHxLHMFNi/VFGnc=', 'PATIENT', 'P001')
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Doctor user mapped to S001 (doc123)
INSERT INTO user_accounts (email, password_hash, role, specialist_id)
VALUES ('drjane@example.com', 'bkieWXKmgM0rivY55nL2Fw==:27t1MOiPxsQlBv1Y99+L4kphGtxfCpiT/8pMGO3tzIQ=', 'SPECIALIST', 'S001')
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Sample appointments
INSERT INTO appointments (patient_id, specialist_id, date_time, reason, status) VALUES
('P001','S001','2025-10-09 09:30:00','Routine Check-up','BOOKED'),
('P001','S001','2025-10-02 11:00:00','Follow-up Visit','COMPLETED');

-- Sample prescription linked to latest appointment
INSERT INTO prescriptions (appointment_id, patient_id, specialist_id, medication, dosage, days, issued_at, status)
SELECT a.id, 'P001','S001','Amoxicillin','500 mg',5,'2025-10-02 11:10:00','ACTIVE'
FROM appointments a
WHERE a.patient_id='P001' AND a.specialist_id='S001'
ORDER BY a.date_time DESC LIMIT 1;

-- Sample vitals
INSERT INTO vitals (patient_id, type, value, captured_at) VALUES
('P001','BP','120/80','2025-10-01 08:30:00'),
('P001','HR','78 bpm','2025-10-01 08:31:00'),
('P001','TEMP','36.9 C','2025-10-01 08:32:00');
