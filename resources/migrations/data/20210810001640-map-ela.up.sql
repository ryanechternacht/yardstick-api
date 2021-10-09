insert into assessment_map_v1 (
  school_assessment_instance_id,
  TestRITScore,
  TestPercentile,
  TestDurationMinutes,
  ProjectedProficiencyStudy1,
  ProjectedProficiencyLevel1,
  ProjectedProficiencyStudy2,
  ProjectedProficiencyLevel2
) values 
(7, 190, 33, 68, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Below Average'),
(8, 193, 26, 71, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Below Average'),
(9, 195, 37, 62, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Below Average'),
(10, 192, 29, 37, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Below Average'),
(11, 197, 39, 84, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Below Average');
--;;
insert into assessment_map_v1 (
  school_assessment_instance_id,
  TermName,
  Goal1Name,
  Goal1RitScore,
  Goal2Name,
  Goal2RitScore,
  Goal3Name,
  Goal3RitScore,
  Goal4Name,
  Goal4RitScore,
  ProjectedProficiencyStudy1,
  ProjectedProficiencyLevel1,
  ProjectedProficiencyStudy2,
  ProjectedProficiencyLevel2,
  TestRITScore,
  TestPercentile,
  TestDurationMinutes
) values (
  12,
  'Winter 2021',
  'Reading',
  272,
  'Critical Thinking',
  273,
  'Creative Writing',
  274,
  'Science Fiction',
  278,
  'ACT College Readiness',
  'Off track',
  'Forward Projection',
  'At Risk',
  199,
  36,
  73
);


