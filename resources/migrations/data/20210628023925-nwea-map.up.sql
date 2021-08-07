-- TODO add some reading data in here

insert into assessment_map_v1 (
  student_assessment_id,
  TestRITScore,
  TestPercentile,
  TestDurationMinutes,
  ProjectedProficiencyStudy1,
  ProjectedProficiencyLevel1,
  ProjectedProficiencyStudy2,
  ProjectedProficiencyLevel2
) values 
(1, 226, 65, 68, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Proficient'),
(2, 228, 82, 71, 'ACT College Readiness', 'On Track for a 22', 'Forward Projection', 'Advanced'),
(3, 229, 51, 62, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Proficient'),
(4, 230, 52, 37, 'ACT College Readiness', 'On Track for a 24', 'Forward Projection', 'Proficient'),
(5, 227, 98, 84, 'ACT College Readiness', 'Not on Track', 'Forward Projection', 'Proficient');
--;;
insert into assessment_map_v1 (
  student_assessment_id,
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
  6,
  'Winter 2021',
  'Geometry',
  278,
  'Statistics and Probability',
  282,
  'Operations and Algebra Thinking',
  283,
  'The Real and Complex Number System',
  284,
  'ACT College Readiness',
  'On Track for a 24',
  'Forward Projection',
  'Proficient',
  232,
  98,
  73
);

