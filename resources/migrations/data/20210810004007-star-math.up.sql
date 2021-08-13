insert into assessment_star_v1 (
  student_assessment_id,
  scaled_score,
  test_duration,
  percentile_rank,
  screening_category,
  state_benchmark,
  current_sgp
) values
(29, 704, NULL, 75, 'At/Above Benchmark',	'Accelerated', 85),
(30, 700, NULL, 76, 'At/Above Benchmark',	'Accelerated', 83),
(31, 715, NULL, 79, 'At/Above Benchmark',	'Accelerated', 87),
(32, 696, NULL, 81, 'At/Above Benchmark',	'Accelerated', 81),
(33, 710, NULL, 74, 'At/Above Benchmark',	'Accelerated', 84),
(34, 717, NULL, 76, 'At/Above Benchmark',	'Accelerated', 86),
(35, 720, NULL, 78, 'At/Above Benchmark',	'Accelerated', 89),
(36, 725, NULL, 73, 'At/Above Benchmark',	'Accelerated', 91),
(37, 704, NULL, 69, 'At/Above Benchmark',	'Accelerated', 84),
(38, 700, NULL, 68, 'At/Above Benchmark',	'Accelerated', 82),
(39, 708, NULL, 60, 'At/Above Benchmark',	'Accelerated', 85),
(40, 709, NULL, 82, 'At/Above Benchmark',	'Accelerated', 83),
(41, 704, NULL, 94, 'At/Above Benchmark',	'Accelerated', 82),
(42, 712, NULL, 83, 'At/Above Benchmark',	'Accelerated', 89),
(43, 704, NULL, 86, 'At/Above Benchmark',	'Accelerated', 84);
--;;
insert into assessment_star_v1 (
  student_assessment_id,
  scaled_score,
  test_duration,
  literacy_classification,
  irl,
  lower_zpd,
  upper_zpd,
  percentile_rank,
  screening_category,
  state_benchmark,
  current_sgp
) values 
(44, 712, NULL, NULL, NULL, 2.6, 3.7, 85, 'At/Above Benchmark',	'Accelerated', 83);
