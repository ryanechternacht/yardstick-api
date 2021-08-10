insert into assessment_star_v1 (
  student_assessment_id,
  scaled_score,
  test_duration,
  percentile_rank,
  screening_category,
  state_benchmark,
  current_sgp
) values
(29, 330, NULL, 75, 'At/Above Benchmark',	'Accelerated', 85),
(30, 331, NULL, 76, 'At/Above Benchmark',	'Accelerated', 83),
(31, 332, NULL, 79, 'At/Above Benchmark',	'Accelerated', 87),
(32, 333, NULL, 81, 'At/Above Benchmark',	'Accelerated', 81),
(33, 334, NULL, 74, 'At/Above Benchmark',	'Accelerated', 84),
(34, 335, NULL, 76, 'At/Above Benchmark',	'Accelerated', 86),
(35, 336, NULL, 78, 'At/Above Benchmark',	'Accelerated', 89),
(36, 337, NULL, 73, 'At/Above Benchmark',	'Accelerated', 91),
(37, 338, NULL, 69, 'At/Above Benchmark',	'Accelerated', 84),
(38, 339, NULL, 68, 'At/Above Benchmark',	'Accelerated', 82),
(39, 340, NULL, 60, 'At/Above Benchmark',	'Accelerated', 85),
(40, 341, NULL, 82, 'At/Above Benchmark',	'Accelerated', 83),
(41, 342, NULL, 94, 'At/Above Benchmark',	'Accelerated', 82),
(42, 343, NULL, 83, 'At/Above Benchmark',	'Accelerated', 89),
(43, 344, NULL, 86, 'At/Above Benchmark',	'Accelerated', 84);
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
(44, 345, NULL, NULL, NULL, 2.6, 3.7, 85, 'At/Above Benchmark',	'Accelerated', 83);
