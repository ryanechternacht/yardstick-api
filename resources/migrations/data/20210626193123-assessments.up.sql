insert into academic_year (id, start_year, end_year, name, short_name) values
(2020, 2019, 2020, '2019 - 2020', '''19-''20'),
(2021, 2020, 2021, '2020 - 2021', '''20-''21');
--;;
insert into assessment (name, release, assessment_table, subject, type, short_name, scale) values
('NWEA MAP - Mathematics', 'MAP 2021', 'assessment_map_v1', 'math', 'growth', 'MAP', 'RIT Score'),
('NWEA MAP - ELA', 'MAP 2021', 'assessment_map_v1', 'ela', 'growth', 'MAP', 'RIT Score'),
('STAR - ELA', 'STAR 2021', 'assessment_star_v1', 'ela', 'growth', 'STAR', 'Score'),
('STAR - Mathematics', 'STAR 2021', 'assessment_star_v1', 'math', 'growth', 'STAR', 'Score');
--;;
insert into assessment_period (assessment_id, ordering, name) values
(1, 1, 'Fall'),
(1, 2, 'Winter'),
(1, 3, 'Spring'),
(2, 1, 'Fall'),
(2, 2, 'Winter'),
(2, 3, 'Spring'),
(3, 1, 'September'),
(3, 2, 'October'),
(3, 3, 'November'),
(3, 4, 'December'),
(3, 5, 'January'),
(3, 6, 'February'),
(3, 7, 'March'),
(3, 8, 'April'),
(4, 1, 'September'),
(4, 2, 'October'),
(4, 3, 'November'),
(4, 4, 'December'),
(4, 5, 'January'),
(4, 6, 'February'),
(4, 7, 'March'),
(4, 8, 'April');
--;;
-- TODO rewrite these using selects?
insert into school_assessment_instance (academic_year_id, assessment_period_id, school_id) values 
-- MAP 
(2020, 1, 1), (2020, 2, 1), (2020, 3, 1), (2021, 1, 1), (2021, 2, 1), (2021, 3, 1),
(2020, 4, 1), (2020, 5, 1), (2020, 6, 1), (2021, 4, 1), (2021, 5, 1), (2021, 6, 1),
-- STAR
(2020, 7, 1), (2020, 8, 1), (2020, 9, 1), (2020, 10, 1), (2020, 11, 1), (2020, 12, 1), (2020, 13, 1), (2020, 14, 1),
(2021, 7, 1), (2021, 8, 1), (2021, 9, 1), (2021, 10, 1), (2021, 11, 1), (2021, 12, 1), (2021, 13, 1), (2021, 14, 1),
(2020, 15, 1), (2020, 16, 1), (2020, 17, 1), (2020, 18, 1), (2020, 19, 1), (2020, 20, 1), (2020, 21, 1), (2020, 22, 1),
(2021, 15, 1), (2021, 16, 1), (2021, 17, 1), (2021, 18, 1), (2021, 19, 1), (2021, 20, 1), (2021, 21, 1), (2021, 22, 1);
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
(13, 1, 7, 21, 'assessment_star_v1', 1, '2019-09-01'),
(14, 1, 7, 22, 'assessment_star_v1', 1, '2019-10-01'),
(15, 1, 7, 23, 'assessment_star_v1', 1, '2019-11-01'),
(16, 1, 7, 24, 'assessment_star_v1', 1, '2019-12-01'),
(17, 1, 7, 25, 'assessment_star_v1', 1, '2020-01-01'),
(18, 1, 7, 26, 'assessment_star_v1', 1, '2020-02-01'),
(19, 1, 7, 27, 'assessment_star_v1', 1, '2020-03-01'),
(20, 1, 7, 28, 'assessment_star_v1', 1, '2020-04-01'),
(21, 1, 8, 29, 'assessment_star_v1', 1, '2020-09-01'),
(22, 1, 8, 30, 'assessment_star_v1', 1, '2020-10-01'),
(23, 1, 8, 31, 'assessment_star_v1', 1, '2020-11-01'),
(24, 1, 8, 32, 'assessment_star_v1', 1, '2020-12-01'),
(25, 1, 8, 33, 'assessment_star_v1', 1, '2021-01-01'),
(26, 1, 8, 34, 'assessment_star_v1', 1, '2021-02-01'),
(27, 1, 8, 35, 'assessment_star_v1', 1, '2021-03-01'),
(28, 1, 8, 36, 'assessment_star_v1', 1, '2021-04-01');
