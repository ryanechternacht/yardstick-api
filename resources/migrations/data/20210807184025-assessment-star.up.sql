insert into assessment_star_v1 (
  student_assessment_id,
  scaled_score,
  test_duration,
  percentile_rank,
  screening_category,
  state_benchmark,
  current_sgp
) values
(14, 330, NULL, 16, 'Intervention', 'Limited', 1),
(15, 331, NULL, 16, 'Intervention', 'Limited', 2),
(16, 332, NULL, 16, 'Intervention', 'Limited', 3),
(17, 333, NULL, 16, 'Intervention', 'Limited', 4),
(18, 334, NULL, 16, 'Intervention', 'Limited', 5),
(19, 335, NULL, 16, 'Intervention', 'Limited', 6),
(20, 336, NULL, 16, 'Intervention', 'Limited', 7),
(21, 337, NULL, 16, 'Intervention', 'Limited', 8),
(22, 338, NULL, 16, 'Intervention', 'Limited', 9),
(23, 339, NULL, 16, 'Intervention', 'Limited', 10),
(24, 340, NULL, 16, 'Intervention', 'Limited', 9),
(25, 341, NULL, 16, 'Intervention', 'Limited', 8),
(26, 342, NULL, 16, 'Intervention', 'Limited', 7),
(27, 343, NULL, 16, 'Intervention', 'Limited', 6),
(28, 344, NULL, 16, 'Intervention', 'Limited', 5);
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
(29, 345, NULL, NULL, NULL, 2.6, 3.7, 16, 'Intervention', 'Limited', 4);
