create table assessment_star_v1 (
  id uuid not null default uuid_generate_v4(),
  constraint assessment_star_v1_id primary key (id),
  school_assessment_instance_id int not null references school_assessment_instance(id),
  studentID text,
  StudentID2 text,
  CurrentGrade text,
  assessment_subject text,
  student_first_name text,
  student_last_name text,
  teacher_last_name text,
  assessment_date text,
  scaled_score text,
  test_duration int,
  literacy_classification text,
  irl text,
  lower_zpd decimal,
  upper_zpd decimal,
  percentile_rank int,
  screening_category text,
  state_benchmark text,
  current_sgp int,

  -- TODO I don't love having assessment_date in this, but it's allowed
  -- in the extracts for a student to take it more than once in a period
  -- and all attempts are returned
  constraint assessment_star_v1_school_assessment_student_unique UNIQUE
    (school_assessment_instance_id, studentID, studentID2, assessment_date)
);