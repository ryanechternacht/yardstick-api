insert into assessment_star_v1 (
  school_assessment_instance_id,
  scaled_score,
  test_duration,
  percentile_rank,
  screening_category,
  state_benchmark,
  current_sgp
) values
(13, 330, NULL, 16, 'Intervention', 'Limited', 1),
(14, 331, NULL, 16, 'Intervention', 'Limited', 2),
(15, 332, NULL, 16, 'Intervention', 'Limited', 3),
(16, 333, NULL, 16, 'Intervention', 'Limited', 4),
(17, 334, NULL, 16, 'Intervention', 'Limited', 5),
(18, 335, NULL, 16, 'Intervention', 'Limited', 6),
(19, 336, NULL, 16, 'Intervention', 'Limited', 7),
(20, 337, NULL, 16, 'Intervention', 'Limited', 8),
(21, 338, NULL, 16, 'Intervention', 'Limited', 9),
(22, 339, NULL, 16, 'Intervention', 'Limited', 10),
(23, 340, NULL, 16, 'Intervention', 'Limited', 9),
(24, 341, NULL, 16, 'Intervention', 'Limited', 8),
(25, 342, NULL, 16, 'Intervention', 'Limited', 7),
(26, 343, NULL, 16, 'Intervention', 'Limited', 6),
(27, 344, NULL, 16, 'Intervention', 'Limited', 5);
--;;
insert into assessment_star_v1 (
  school_assessment_instance_id,
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
(28, 345, NULL, NULL, NULL, 2.6, 3.7, 16, 'Intervention', 'Limited', 4);
