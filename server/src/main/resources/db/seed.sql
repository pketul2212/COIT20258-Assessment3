
INSERT IGNORE INTO users(username, pwd_hash, role) VALUES
('patient1', '$PBKDF2$10000$WiwUeHhH$4g3Q1JtJ9XfHk0bW0bR8rQ==', 'PATIENT'),
('spec1',    '$PBKDF2$10000$WiwUeHhH$4g3Q1JtJ9XfHk0bW0bR8rQ==', 'SPECIALIST'),
('staff1',   '$PBKDF2$10000$WiwUeHhH$4g3Q1JtJ9XfHk0bW0bR8rQ==', 'STAFF');

INSERT IGNORE INTO patients(user_id, name, dob, contact)
SELECT user_id, 'Jane Patient', '1995-06-15', 'jane@example.com' FROM users WHERE username='patient1';

INSERT IGNORE INTO specialists(user_id, name, specialty)
SELECT user_id, 'Dr. Spec One', 'General Medicine' FROM users WHERE username='spec1';

-- Availability slots (next week)
INSERT INTO availability(specialist_id, start_at, end_at) VALUES
((SELECT specialist_id FROM specialists LIMIT 1), DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY)+INTERVAL 30 MINUTE),
((SELECT specialist_id FROM specialists LIMIT 1), DATE_ADD(NOW(), INTERVAL 2 DAY)+INTERVAL 1 HOUR, DATE_ADD(NOW(), INTERVAL 2 DAY)+INTERVAL 90 MINUTE);
