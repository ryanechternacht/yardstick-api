create table assessment_star_v1 (
  id serial primary key, -- will probably need to be uuid eventually
  school_assessment_instance_id int not null references school_assessment_instance(id),
  studentID text,
  StudentID2 text,
  CurrentGrade text,
  assessment_subject text,
  student_first_name text,
  student_last_name text,
  teacher_last_name text,
  assessment_date timestamp without time zone,
  scaled_score int,
  test_duration int,
  literacy_classification text,
  irl text,
  lower_zpd decimal,
  upper_zpd decimal,
  percentile_rank int,
  screening_category text,
  state_benchmark text,
  current_sgp int,

  constraint assessment_star_v1_school_assessment_student_unique UNIQUE
    (school_assessment_instance_id, studentID, studentID2, assessment_date)
);