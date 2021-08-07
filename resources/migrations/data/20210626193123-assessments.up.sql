insert into academic_year (id, start_year, end_year, name, short_name) values
(2020, 2019, 2020, '2019 - 2020', '''19-''20'),
(2021, 2020, 2021, '2020 - 2021', '''20-''21');
--;;
insert into assessment (name, release, assessment_table, subject, type, short_name, scale) values
('NWEA MAP - Mathematics', 'MAP 2021', 'assessment_map_v1', 'math', 'growth', 'MAP', 'RIT Score'),
('NWEA MAP - ELA', 'MAP 2021', 'assessment_map_v1', 'ela', 'growth', 'Forward', 'Fs'),
('PreACT 8/9', 'PreACT 8/9', 'assessment_preact_v1', 'general', 'summative', 'PreACT', 'Points'),
('Forward - Mathematics', 'Forward - Mathematics', 'assessment_forward_math_v1', 'math', 'growth', 'MAP', 'RIT Score'),
('Forward - ELA', 'Forward - ELA', 'assessment_forward_ela_v1', 'ela', 'growth', 'Forward', 'Fs');
--;;
insert into assessment_term (assessment_id, ordering, name) values
(1, 1, 'Fall'),
(1, 2, 'Winter'),
(1, 3, 'Spring'),
(2, 1, 'Fall'),
(2, 2, 'Winter'),
(2, 3, 'Spring'),
(3, 1, 'Attempt'),
(4, 1, '??'),
(5, 1, '??');
--;;
insert into assessment_instance (academic_year_id, assessment_term_id) values
(2020, 1), (2020, 2), (2020, 3), (2021, 1), (2021, 2), (2021, 3),
(2020, 4), (2020, 5), (2020, 6), (2021, 4), (2021, 5), (2021, 6),
(2021, 7),
(2021, 8),
(2021, 9);
--;;
insert into school_assessment_instance (assessment_instance_id, school_id) values 
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1),
(9, 1), (10, 1), (11, 1), (12, 1), (13, 1), (14, 1), (15, 1);
--;;
insert into student_assessment 
(school_assessment_instance_id, student_id, grade_id, yardstick_performance_rating, assessment_table, assessment_table_id, date_taken) values
(1, 1, 7, 85, 'assessment_map_v1', 1, '2019-09-01'),
(2, 1, 7, 85, 'assessment_map_v1', 2, '2019-12-31'),
(3, 1, 7, 85, 'assessment_map_v1', 3, '2020-04-01'),
(4, 1, 8, 85, 'assessment_map_v1', 4, '2020-09-01'),
(5, 1, 8, 85, 'assessment_map_v1', 5, '2020-12-31'),
(6, 1, 8, 85, 'assessment_map_v1', 6, '2021-04-01'),
(7, 1, 7, 15, 'assessment_map_v1', 1, '2019-09-01'),
(8, 1, 7, 15, 'assessment_map_v1', 2, '2019-12-31'),
(9, 1, 7, 15, 'assessment_map_v1', 3, '2020-04-01'),
(10, 1, 8, 15, 'assessment_map_v1', 4, '2020-09-01'),
(11, 1, 8, 15, 'assessment_map_v1', 5, '2020-12-31'),
(12, 1, 8, 15, 'assessment_map_v1', 6, '2021-04-01'),
(13, 1, 8, 50, 'assessment_preact_v1', 1, '2021-04-01'),
(14, 1, 8, 75, 'assessment_forward_ela_v1', 1, '2021-04-01'),
(15, 1, 8, 30, 'assessment_forward_math_v1', 1, '2021-04-01');
