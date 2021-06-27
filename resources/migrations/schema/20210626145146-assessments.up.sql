create table academic_year (
  id int primary key, -- set manually, should be 2020 for the 2019-2020 year
  name text not null, -- '2019 - 2020'
  short_name text not null, --"'19-'20"?
  start_year int not null,
  end_year int not null
);
--;;
create table assessment (
  id serial primary key,
  name text not null,
  release text not null, -- for tracking versions, editions, etc. e.g. "2014"
  assessment_table text not null -- e.g. "assessment_map_v1"
);
--;;
create table assessment_term (
  id serial primary key,
  assessment_id int not null references assessment(id),
  ordering int not null,
  name text not null
  -- TODO another name?
);
--;;
create table assessment_instance (
  id serial primary key,
  academic_year_id int not null references academic_year(id),
  assessment_term_id int not null references assessment_term(id)
);
--;;
-- TODO This name is great from a domain standpoint, and awful from a relational standpoint
create table student_assessment (
  id serial primary key,
  assessment_instance_id int not null references assessment_instance(id),
  student_id int references student(id), -- can be null
  local_student_id text,
  state_student_id text,
  yardstick_performance_rating decimal, -- null because it's calced after the fact
  assessment_table text not null, -- same as assessment.assessment_table
  assessment_table_id int not null,
  date_taken timestamp with time zone,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now()
);
