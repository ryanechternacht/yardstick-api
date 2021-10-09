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
(school_assessment_instance_id, student_id, grade_id, yardstick_performance_rating, 
  assessment_table, assessment_table_id, date_taken, attempts) values
(1, 1, 7, 85, 'assessment_map_v1', 1, '2019-09-01', 1),
(2, 1, 7, 85, 'assessment_map_v1', 2, '2019-12-31', 1),
(3, 1, 7, 85, 'assessment_map_v1', 3, '2020-04-01', 1),
(4, 1, 8, 85, 'assessment_map_v1', 4, '2020-09-01', 1),
(5, 1, 8, 85, 'assessment_map_v1', 5, '2020-12-31', 1),
(6, 1, 8, 85, 'assessment_map_v1', 6, '2021-04-01', 1),
(7, 1, 7, 15, 'assessment_map_v1', 7, '2019-09-01', 1),
(8, 1, 7, 15, 'assessment_map_v1', 8, '2019-12-31', 1),
(9, 1, 7, 15, 'assessment_map_v1', 9, '2020-04-01', 1),
(10, 1, 8, 15, 'assessment_map_v1', 10, '2020-09-01', 1),
(11, 1, 8, 15, 'assessment_map_v1', 11, '2020-12-31', 1),
(12, 1, 8, 15, 'assessment_map_v1', 12, '2021-04-01', 1),
(13, 1, 7, 21, 'assessment_star_v1', 1, '2019-09-01', 1),
(14, 1, 7, 22, 'assessment_star_v1', 2, '2019-10-01', 1),
(15, 1, 7, 23, 'assessment_star_v1', 3, '2019-11-01', 1),
(16, 1, 7, 24, 'assessment_star_v1', 4, '2019-12-01', 1),
(17, 1, 7, 25, 'assessment_star_v1', 5, '2020-01-01', 1),
(18, 1, 7, 26, 'assessment_star_v1', 6, '2020-02-01', 1),
(19, 1, 7, 27, 'assessment_star_v1', 7, '2020-03-01', 1),
(20, 1, 7, 28, 'assessment_star_v1', 8, '2020-04-01', 1),
(21, 1, 8, 29, 'assessment_star_v1', 9, '2020-09-01', 1),
(22, 1, 8, 30, 'assessment_star_v1', 10, '2020-10-01', 1),
(23, 1, 8, 31, 'assessment_star_v1', 11, '2020-11-01', 1),
(24, 1, 8, 32, 'assessment_star_v1', 12, '2020-12-01', 1),
(25, 1, 8, 33, 'assessment_star_v1', 13, '2021-01-01', 1),
(26, 1, 8, 34, 'assessment_star_v1', 14, '2021-02-01', 1),
(27, 1, 8, 35, 'assessment_star_v1', 15, '2021-03-01', 1),
(28, 1, 8, 36, 'assessment_star_v1', 16, '2021-04-01', 1),
(29, 1, 7, 74, 'assessment_star_v1', 17, '2019-09-01', 1),
(30, 1, 7, 72, 'assessment_star_v1', 18, '2019-10-01', 1),
(31, 1, 7, 65, 'assessment_star_v1', 19, '2019-11-01', 1),
(32, 1, 7, 77, 'assessment_star_v1', 20, '2019-12-01', 1),
(33, 1, 7, 63, 'assessment_star_v1', 21, '2020-01-01', 1),
(34, 1, 7, 72, 'assessment_star_v1', 22, '2020-02-01', 1),
(35, 1, 7, 79, 'assessment_star_v1', 23, '2020-03-01', 1),
(36, 1, 7, 74, 'assessment_star_v1', 24, '2020-04-01', 1),
(37, 1, 8, 69, 'assessment_star_v1', 25, '2020-09-01', 1),
(38, 1, 8, 66, 'assessment_star_v1', 26, '2020-10-01', 1),
(39, 1, 8, 62, 'assessment_star_v1', 27, '2020-11-01', 1),
(40, 1, 8, 65, 'assessment_star_v1', 28, '2020-12-01', 1),
(41, 1, 8, 67, 'assessment_star_v1', 29, '2021-01-01', 1),
(42, 1, 8, 68, 'assessment_star_v1', 30, '2021-02-01', 1),
(43, 1, 8, 71, 'assessment_star_v1', 31, '2021-03-01', 1),
(44, 1, 8, 72, 'assessment_star_v1', 32, '2021-04-01', 1);
