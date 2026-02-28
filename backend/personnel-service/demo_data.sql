-- ============================================
-- DEMO DATA FOR PERSONNEL SERVICE
-- ============================================
-- This script contains realistic demo data for disaster management personnel
-- Generated on: February 15, 2026
-- ============================================

-- ============================================
-- 1. MEDICAL CONDITIONS
-- ============================================

INSERT INTO medical_condition (id, blood_group, height, weight, created_at, updated_at, is_disabled) VALUES
(1, 'O+', '175 cm', '75 kg', NOW(), NOW(), false),
(2, 'A+', '168 cm', '68 kg', NOW(), NOW(), false),
(3, 'B+', '180 cm', '82 kg', NOW(), NOW(), false),
(4, 'AB+', '165 cm', '62 kg', NOW(), NOW(), false),
(5, 'O-', '178 cm', '80 kg', NOW(), NOW(), false),
(6, 'A-', '172 cm', '70 kg', NOW(), NOW(), false),
(7, 'B-', '182 cm', '85 kg', NOW(), NOW(), false),
(8, 'AB-', '170 cm', '72 kg', NOW(), NOW(), false),
(9, 'O+', '176 cm', '78 kg', NOW(), NOW(), false),
(10, 'A+', '169 cm', '65 kg', NOW(), NOW(), false);

-- ============================================
-- 2. PERSONS
-- ============================================

INSERT INTO person (id, personal_code, first_name, last_name, phone, email, address, role, department, organization, rank, medical_condition_id, status, shift_start_time, shift_end_time, created_at, updated_at, is_disabled) VALUES
(1, 'PER-001', 'John', 'Silva', '+94771234567', 'john.silva@dmc.gov.lk', '123 Galle Road, Colombo 03', 'Rescue Specialist', 'Emergency Response', 'Disaster Management Center', 'Captain', 1, 'Available', '2026-02-15 08:00:00', '2026-02-15 16:00:00', NOW(), NOW(), false),
(2, 'PER-002', 'Sarah', 'Fernando', '+94772345678', 'sarah.fernando@dmc.gov.lk', '456 Kandy Road, Kandy', 'Medical Officer', 'Medical Response', 'Disaster Management Center', 'Lieutenant', 2, 'Available', '2026-02-15 08:00:00', '2026-02-15 16:00:00', NOW(), NOW(), false),
(3, 'PER-003', 'Michael', 'Perera', '+94773456789', 'michael.perera@dmc.gov.lk', '789 Galle Road, Galle', 'Fire Fighter', 'Fire Department', 'National Fire Service', 'Senior Officer', 3, 'On Duty', '2026-02-15 16:00:00', '2026-02-16 00:00:00', NOW(), NOW(), false),
(4, 'PER-004', 'Emily', 'Jayawardena', '+94774567890', 'emily.jay@dmc.gov.lk', '321 Main Street, Jaffna', 'Paramedic', 'Medical Response', 'Disaster Management Center', 'Corporal', 4, 'Available', '2026-02-15 08:00:00', '2026-02-15 16:00:00', NOW(), NOW(), false),
(5, 'PER-005', 'David', 'Rajapaksa', '+94775678901', 'david.raj@dmc.gov.lk', '654 Beach Road, Negombo', 'Search and Rescue', 'Emergency Response', 'Navy Rescue Unit', 'Commander', 5, 'Available', '2026-02-15 00:00:00', '2026-02-15 08:00:00', NOW(), NOW(), false),
(6, 'PER-006', 'Priya', 'Wickramasinghe', '+94776789012', 'priya.wick@dmc.gov.lk', '987 Hill Street, Nuwara Eliya', 'Communications Officer', 'Communications', 'Disaster Management Center', 'Lieutenant', 6, 'On Leave', '2026-02-15 08:00:00', '2026-02-15 16:00:00', NOW(), NOW(), false),
(7, 'PER-007', 'Robert', 'De Silva', '+94777890123', 'robert.desilva@dmc.gov.lk', '147 Park Avenue, Matara', 'Logistics Coordinator', 'Logistics', 'Disaster Management Center', 'Major', 7, 'Available', '2026-02-15 08:00:00', '2026-02-15 16:00:00', NOW(), NOW(), false),
(8, 'PER-008', 'Anjali', 'Gunawardena', '+94778901234', 'anjali.guna@dmc.gov.lk', '258 Temple Road, Anuradhapura', 'Structural Engineer', 'Infrastructure', 'Public Works Department', 'Senior Engineer', 8, 'Available', '2026-02-15 08:00:00', '2026-02-15 16:00:00', NOW(), NOW(), false),
(9, 'PER-009', 'James', 'Bandara', '+94779012345', 'james.bandara@dmc.gov.lk', '369 Lake Road, Kurunegala', 'Hazmat Specialist', 'Hazardous Materials', 'Environmental Protection', 'Captain', 9, 'Available', '2026-02-15 16:00:00', '2026-02-16 00:00:00', NOW(), NOW(), false),
(10, 'PER-010', 'Nina', 'Samaraweera', '+94770123456', 'nina.sam@dmc.gov.lk', '741 Station Road, Ratnapura', 'K9 Handler', 'Search and Rescue', 'Police K9 Unit', 'Sergeant', 10, 'Available', '2026-02-15 08:00:00', '2026-02-15 16:00:00', NOW(), NOW(), false);

-- ============================================
-- 3. SKILLS
-- ============================================

INSERT INTO skill (id, experience_years, mission_count, profession, level, created_at, updated_at, is_disabled, person_id) VALUES
-- John Silva (Rescue Specialist)
(1, 8, 45, 'Urban Search and Rescue', 'Expert', NOW(), NOW(), false, 1),
(2, 8, 45, 'Rope Rescue', 'Expert', NOW(), NOW(), false, 1),
(3, 6, 30, 'First Aid', 'Advanced', NOW(), NOW(), false, 1),

-- Sarah Fernando (Medical Officer)
(4, 10, 60, 'Emergency Medicine', 'Expert', NOW(), NOW(), false, 2),
(5, 10, 60, 'Trauma Care', 'Expert', NOW(), NOW(), false, 2),
(6, 8, 50, 'Triage', 'Expert', NOW(), NOW(), false, 2),

-- Michael Perera (Fire Fighter)
(7, 12, 80, 'Fire Suppression', 'Expert', NOW(), NOW(), false, 3),
(8, 12, 80, 'Hazmat Response', 'Advanced', NOW(), NOW(), false, 3),
(9, 10, 65, 'Technical Rescue', 'Advanced', NOW(), NOW(), false, 3),

-- Emily Jayawardena (Paramedic)
(10, 5, 35, 'Advanced Life Support', 'Advanced', NOW(), NOW(), false, 4),
(11, 5, 35, 'Patient Transport', 'Expert', NOW(), NOW(), false, 4),
(12, 4, 28, 'CPR', 'Expert', NOW(), NOW(), false, 4),

-- David Rajapaksa (Search and Rescue)
(13, 15, 100, 'Water Rescue', 'Expert', NOW(), NOW(), false, 5),
(14, 15, 100, 'Diving', 'Expert', NOW(), NOW(), false, 5),
(15, 12, 85, 'Navigation', 'Expert', NOW(), NOW(), false, 5),

-- Priya Wickramasinghe (Communications Officer)
(16, 7, 40, 'Radio Operations', 'Expert', NOW(), NOW(), false, 6),
(17, 7, 40, 'Satellite Communications', 'Advanced', NOW(), NOW(), false, 6),
(18, 5, 30, 'Emergency Broadcasting', 'Advanced', NOW(), NOW(), false, 6),

-- Robert De Silva (Logistics Coordinator)
(19, 9, 55, 'Supply Chain Management', 'Expert', NOW(), NOW(), false, 7),
(20, 9, 55, 'Resource Allocation', 'Expert', NOW(), NOW(), false, 7),
(21, 7, 45, 'Inventory Management', 'Advanced', NOW(), NOW(), false, 7),

-- Anjali Gunawardena (Structural Engineer)
(22, 11, 70, 'Structural Assessment', 'Expert', NOW(), NOW(), false, 8),
(23, 11, 70, 'Building Safety', 'Expert', NOW(), NOW(), false, 8),
(24, 9, 60, 'Demolition Planning', 'Advanced', NOW(), NOW(), false, 8),

-- James Bandara (Hazmat Specialist)
(25, 13, 90, 'Chemical Response', 'Expert', NOW(), NOW(), false, 9),
(26, 13, 90, 'Decontamination', 'Expert', NOW(), NOW(), false, 9),
(27, 10, 75, 'Environmental Monitoring', 'Advanced', NOW(), NOW(), false, 9),

-- Nina Samaraweera (K9 Handler)
(28, 6, 42, 'K9 Search', 'Expert', NOW(), NOW(), false, 10),
(29, 6, 42, 'Cadaver Detection', 'Advanced', NOW(), NOW(), false, 10),
(30, 5, 35, 'K9 Training', 'Advanced', NOW(), NOW(), false, 10);

-- ============================================
-- 4. EMERGENCY CONTACTS
-- ============================================

INSERT INTO emergency_contact (id, name, telephone, address, relation, note, created_at, updated_at, is_disabled, person_id) VALUES
-- John Silva
(1, 'Maria Silva', '+94711234567', '123 Galle Road, Colombo 03', 'Spouse', 'Primary contact', NOW(), NOW(), false, 1),
(2, 'Peter Silva', '+94712234567', '456 Lake Drive, Colombo 05', 'Father', 'Secondary contact', NOW(), NOW(), false, 1),

-- Sarah Fernando
(3, 'Rohan Fernando', '+94712345678', '456 Kandy Road, Kandy', 'Spouse', 'Primary contact', NOW(), NOW(), false, 2),
(4, 'Anita Fernando', '+94713345678', '789 Hill Road, Kandy', 'Mother', 'Secondary contact', NOW(), NOW(), false, 2),

-- Michael Perera
(5, 'Shani Perera', '+94713456789', '789 Galle Road, Galle', 'Spouse', 'Primary contact', NOW(), NOW(), false, 3),
(6, 'Kumar Perera', '+94714456789', '321 Beach Road, Galle', 'Brother', 'Secondary contact', NOW(), NOW(), false, 3),

-- Emily Jayawardena
(7, 'Dinesh Jayawardena', '+94714567890', '321 Main Street, Jaffna', 'Spouse', 'Primary contact', NOW(), NOW(), false, 4),

-- David Rajapaksa
(8, 'Lakshmi Rajapaksa', '+94715678901', '654 Beach Road, Negombo', 'Spouse', 'Primary contact', NOW(), NOW(), false, 5),
(9, 'Anil Rajapaksa', '+94716678901', '987 Coastal Road, Negombo', 'Father', 'Secondary contact', NOW(), NOW(), false, 5),

-- Priya Wickramasinghe
(10, 'Saman Wickramasinghe', '+94716789012', '987 Hill Street, Nuwara Eliya', 'Spouse', 'Primary contact', NOW(), NOW(), false, 6),

-- Robert De Silva
(11, 'Nimal De Silva', '+94717890123', '147 Park Avenue, Matara', 'Brother', 'Primary contact', NOW(), NOW(), false, 7),

-- Anjali Gunawardena
(12, 'Rajitha Gunawardena', '+94718901234', '258 Temple Road, Anuradhapura', 'Spouse', 'Primary contact', NOW(), NOW(), false, 8),

-- James Bandara
(13, 'Chamari Bandara', '+94719012345', '369 Lake Road, Kurunegala', 'Spouse', 'Primary contact', NOW(), NOW(), false, 9),

-- Nina Samaraweera
(14, 'Kasun Samaraweera', '+94710123456', '741 Station Road, Ratnapura', 'Spouse', 'Primary contact', NOW(), NOW(), false, 10);

-- ============================================
-- 5. ALLERGIES
-- ============================================

INSERT INTO allergy (id, allergen_name, reaction_severity, symptoms, treatment, created_at, updated_at, is_disabled, medical_condition_id) VALUES
(1, 'Penicillin', 'Moderate', 'Skin rash, itching', 'Avoid penicillin antibiotics, use alternatives', NOW(), NOW(), false, 1),
(2, 'Pollen', 'Mild', 'Sneezing, watery eyes', 'Antihistamines as needed', NOW(), NOW(), false, 2),
(3, 'Bee Stings', 'Severe', 'Anaphylaxis risk', 'Carry EpiPen at all times', NOW(), NOW(), false, 4),
(4, 'Latex', 'Moderate', 'Skin irritation, rash', 'Use latex-free gloves', NOW(), NOW(), false, 6),
(5, 'Shellfish', 'Severe', 'Hives, difficulty breathing', 'Strict avoidance, carry EpiPen', NOW(), NOW(), false, 8);

-- ============================================
-- 6. CHRONIC CONDITIONS
-- ============================================

INSERT INTO chronic_condition (id, condition_name, diagnosed_date, current_status, treatment_plan, created_at, updated_at, is_disabled, medical_condition_id) VALUES
(1, 'Asthma', '2020-03-15', 'Controlled', 'Daily inhaler, avoid triggers', NOW(), NOW(), false, 3),
(2, 'Hypertension', '2019-06-20', 'Controlled', 'Daily medication, low sodium diet', NOW(), NOW(), false, 7),
(3, 'Type 2 Diabetes', '2021-01-10', 'Managed', 'Medication, diet control, regular monitoring', NOW(), NOW(), false, 9),
(4, 'Migraine', '2018-11-05', 'Episodic', 'Preventive medication, avoid stress', NOW(), NOW(), false, 10);

-- ============================================
-- 7. PHYSICAL LIMITATIONS
-- ============================================

INSERT INTO physical_limitation (id, limitation_type, description, impact_level, accommodations_needed, created_at, updated_at, is_disabled, medical_condition_id) VALUES
(1, 'Knee Injury', 'Previous ACL repair on right knee', 'Minor', 'Avoid prolonged kneeling, use knee brace for heavy duty', NOW(), NOW(), false, 5),
(2, 'Hearing', 'Slight hearing loss in left ear', 'Mild', 'Use hearing protection, clear communication protocols', NOW(), NOW(), false, 6);

-- ============================================
-- 8. INJURY HISTORY
-- ============================================

INSERT INTO injury_history (id, injury_type, injury_date, treatment_received, recovery_status, notes, created_at, updated_at, is_disabled, medical_condition_id) VALUES
(1, 'Fracture', '2023-05-15', 'Cast for 6 weeks, physiotherapy', 'Fully Recovered', 'Left arm fracture during rescue operation', NOW(), NOW(), false, 1),
(2, 'Burn', '2022-08-20', 'Hospital treatment, skin grafts', 'Fully Recovered', 'Second-degree burns on hands from fire', NOW(), NOW(), false, 3),
(3, 'Concussion', '2024-02-10', 'Rest and monitoring', 'Fully Recovered', 'Head injury from falling debris', NOW(), NOW(), false, 5),
(4, 'Sprain', '2025-11-05', 'RICE protocol, physiotherapy', 'Fully Recovered', 'Ankle sprain during training', NOW(), NOW(), false, 8);

-- ============================================
-- 9. MEDICATIONS
-- ============================================

INSERT INTO medication (id, medication_name, dosage, frequency, start_date, end_date, purpose, side_effects, created_at, updated_at, is_disabled, medical_condition_id) VALUES
(1, 'Salbutamol Inhaler', '100 mcg', 'As needed', '2020-03-15', NULL, 'Asthma relief', 'Tremors, rapid heartbeat', NOW(), NOW(), false, 3),
(2, 'Amlodipine', '5 mg', 'Once daily', '2019-06-20', NULL, 'Blood pressure control', 'Swelling of ankles', NOW(), NOW(), false, 7),
(3, 'Metformin', '500 mg', 'Twice daily', '2021-01-10', NULL, 'Blood sugar control', 'Nausea, diarrhea', NOW(), NOW(), false, 9),
(4, 'Sumatriptan', '50 mg', 'As needed', '2018-11-05', NULL, 'Migraine relief', 'Drowsiness, dizziness', NOW(), NOW(), false, 10);

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Uncomment to verify data insertion:

-- SELECT COUNT(*) as total_persons FROM person;
-- SELECT COUNT(*) as total_skills FROM skill;
-- SELECT COUNT(*) as total_emergency_contacts FROM emergency_contact;
-- SELECT COUNT(*) as available_persons FROM person WHERE status = 'Available';

-- SELECT
--     p.first_name,
--     p.last_name,
--     p.role,
--     p.status,
--     COUNT(s.id) as skill_count
-- FROM person p
-- LEFT JOIN skill s ON p.id = s.person_id
-- GROUP BY p.id, p.first_name, p.last_name, p.role, p.status;

-- ============================================
-- END OF DEMO DATA SCRIPT
-- ============================================
