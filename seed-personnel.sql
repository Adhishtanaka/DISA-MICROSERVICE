-- Medical conditions
INSERT INTO medical_condition (id, blood_group, height, weight, is_disabled, created_at, updated_at) VALUES
(1, 'O+', '180cm', '82kg', false, NOW(), NOW()),
(2, 'A+', '165cm', '70kg', false, NOW(), NOW()),
(3, 'B+', '175cm', '78kg', false, NOW(), NOW()),
(4, 'AB-', '170cm', '65kg', false, NOW(), NOW()),
(5, 'O-', '188cm', '90kg', false, NOW(), NOW()),
(6, 'A-', '160cm', '58kg', false, NOW(), NOW()),
(7, 'B-', '178cm', '85kg', false, NOW(), NOW()),
(8, 'O+', '172cm', '75kg', false, NOW(), NOW()),
(9, 'AB+', '185cm', '88kg', false, NOW(), NOW()),
(10, 'A+', '168cm', '72kg', false, NOW(), NOW());

-- 10 Person records
INSERT INTO person (id, personal_code, first_name, last_name, phone, email, address, role, department, organization, rank, status, shift_start_time, shift_end_time, is_disabled, created_at, updated_at, medical_condition_id) VALUES
(1, 'PER-001', 'John', 'Mitchell', '+94771234501', 'john.mitchell@disa.org', '45 Main St, Colombo', 'RESPONDER', 'Search and Rescue', 'DISA HQ', 'Senior Officer', 'AVAILABLE', '2026-03-03 06:00:00', '2026-03-03 18:00:00', false, NOW(), NOW(), 1),
(2, 'PER-002', 'Sarah', 'Chen', '+94771234502', 'sarah.chen@disa.org', '12 Lake Rd, Kandy', 'COORDINATOR', 'Operations', 'DISA HQ', 'Team Lead', 'AVAILABLE', '2026-03-03 08:00:00', '2026-03-03 20:00:00', false, NOW(), NOW(), 2),
(3, 'PER-003', 'David', 'Perera', '+94771234503', 'david.perera@disa.org', '78 Hill Ave, Galle', 'RESPONDER', 'Medical', 'Red Cross', 'Paramedic', 'ON_DUTY', '2026-03-03 07:00:00', '2026-03-03 19:00:00', false, NOW(), NOW(), 3),
(4, 'PER-004', 'Maria', 'Fernando', '+94771234504', 'maria.fernando@disa.org', '33 Beach Rd, Matara', 'VOLUNTEER', 'Logistics', 'DISA South', 'Volunteer', 'AVAILABLE', '2026-03-03 09:00:00', '2026-03-03 17:00:00', false, NOW(), NOW(), 4),
(5, 'PER-005', 'Kamal', 'Jayawardena', '+94771234505', 'kamal.j@disa.org', '101 Temple Rd, Anuradhapura', 'RESPONDER', 'Search and Rescue', 'DISA North', 'Captain', 'AVAILABLE', '2026-03-03 06:00:00', '2026-03-03 18:00:00', false, NOW(), NOW(), 5),
(6, 'PER-006', 'Lisa', 'De Silva', '+94771234506', 'lisa.desilva@disa.org', '55 Park Lane, Negombo', 'COORDINATOR', 'Communications', 'DISA West', 'Coordinator', 'ON_DUTY', '2026-03-03 08:00:00', '2026-03-03 20:00:00', false, NOW(), NOW(), 6),
(7, 'PER-007', 'Ruwan', 'Bandara', '+94771234507', 'ruwan.b@disa.org', '22 River Rd, Ratnapura', 'RESPONDER', 'Engineering', 'Army Corp', 'Sergeant', 'AVAILABLE', '2026-03-03 05:00:00', '2026-03-03 17:00:00', false, NOW(), NOW(), 7),
(8, 'PER-008', 'Nimal', 'Wijesinghe', '+94771234508', 'nimal.w@disa.org', '89 Station Rd, Kurunegala', 'VOLUNTEER', 'Medical', 'Red Cross', 'Volunteer Medic', 'AVAILABLE', '2026-03-03 07:00:00', '2026-03-03 19:00:00', false, NOW(), NOW(), 8),
(9, 'PER-009', 'Priya', 'Gunawardena', '+94771234509', 'priya.g@disa.org', '14 Flower Rd, Jaffna', 'RESPONDER', 'Search and Rescue', 'DISA North', 'Lieutenant', 'OFF_DUTY', '2026-03-04 06:00:00', '2026-03-04 18:00:00', false, NOW(), NOW(), 9),
(10, 'PER-010', 'Ashan', 'Rajapaksha', '+94771234510', 'ashan.r@disa.org', '67 Fort Rd, Trincomalee', 'RESPONDER', 'Logistics', 'Navy Support', 'Petty Officer', 'AVAILABLE', '2026-03-03 06:00:00', '2026-03-03 18:00:00', false, NOW(), NOW(), 10);

-- Skills (2 per person)
INSERT INTO skill (id, profession, level, experience_years, mission_count, is_disabled, created_at, updated_at, person_id) VALUES
(1, 'Urban Search and Rescue', 'Expert', 12, 45, false, NOW(), NOW(), 1),
(2, 'Rope Rescue', 'Advanced', 8, 20, false, NOW(), NOW(), 1),
(3, 'Disaster Coordination', 'Expert', 15, 60, false, NOW(), NOW(), 2),
(4, 'GIS Mapping', 'Intermediate', 5, 10, false, NOW(), NOW(), 2),
(5, 'Emergency Medicine', 'Expert', 10, 35, false, NOW(), NOW(), 3),
(6, 'Trauma Care', 'Advanced', 7, 25, false, NOW(), NOW(), 3),
(7, 'Supply Chain Management', 'Intermediate', 3, 8, false, NOW(), NOW(), 4),
(8, 'First Aid', 'Advanced', 5, 15, false, NOW(), NOW(), 4),
(9, 'Swift Water Rescue', 'Expert', 14, 50, false, NOW(), NOW(), 5),
(10, 'Heavy Equipment Operation', 'Advanced', 9, 30, false, NOW(), NOW(), 5),
(11, 'Crisis Communication', 'Expert', 11, 40, false, NOW(), NOW(), 6),
(12, 'Radio Operations', 'Advanced', 8, 22, false, NOW(), NOW(), 6),
(13, 'Structural Assessment', 'Expert', 13, 42, false, NOW(), NOW(), 7),
(14, 'Demolition Safety', 'Advanced', 10, 28, false, NOW(), NOW(), 7),
(15, 'Wound Care', 'Intermediate', 4, 12, false, NOW(), NOW(), 8),
(16, 'CPR and AED', 'Advanced', 6, 18, false, NOW(), NOW(), 8),
(17, 'Mountain Rescue', 'Expert', 11, 38, false, NOW(), NOW(), 9),
(18, 'Helicopter Rescue', 'Advanced', 7, 15, false, NOW(), NOW(), 9),
(19, 'Maritime Logistics', 'Expert', 12, 35, false, NOW(), NOW(), 10),
(20, 'Warehouse Management', 'Intermediate', 5, 10, false, NOW(), NOW(), 10);

-- Emergency contacts
INSERT INTO emergency_contact (id, name, telephone, address, relation, note, is_disabled, created_at, updated_at, person_id) VALUES
(1, 'Jane Mitchell', '+94771111101', '45 Main St, Colombo', 'Spouse', '', false, NOW(), NOW(), 1),
(2, 'Wei Chen', '+94771111102', '12 Lake Rd, Kandy', 'Father', '', false, NOW(), NOW(), 2),
(3, 'Nimali Perera', '+94771111103', '78 Hill Ave, Galle', 'Spouse', '', false, NOW(), NOW(), 3),
(4, 'Carlos Fernando', '+94771111104', '33 Beach Rd, Matara', 'Brother', '', false, NOW(), NOW(), 4),
(5, 'Samanthi Jayawardena', '+94771111105', '101 Temple Rd, Anuradhapura', 'Spouse', '', false, NOW(), NOW(), 5),
(6, 'Peter De Silva', '+94771111106', '55 Park Lane, Negombo', 'Father', '', false, NOW(), NOW(), 6),
(7, 'Kumari Bandara', '+94771111107', '22 River Rd, Ratnapura', 'Mother', '', false, NOW(), NOW(), 7),
(8, 'Dilini Wijesinghe', '+94771111108', '89 Station Rd, Kurunegala', 'Sister', '', false, NOW(), NOW(), 8),
(9, 'Sunil Gunawardena', '+94771111109', '14 Flower Rd, Jaffna', 'Father', '', false, NOW(), NOW(), 9),
(10, 'Chamari Rajapaksha', '+94771111110', '67 Fort Rd, Trincomalee', 'Spouse', '', false, NOW(), NOW(), 10);

SELECT setval('medical_condition_id_seq', 10);
SELECT setval('person_id_seq', 10);
SELECT setval('skill_id_seq', 20);
SELECT setval('emergency_contact_id_seq', 10);
