create table obstacle (
  id serial primary key,
  type text not null,
  question text not null,
  answer text not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now()
)
--;;
create table student_obstacle (
  student_id int references student(id),
  obstacle_id int references obstacle(id),
  ordering int not null,
  additional_fields jsonb,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now(),
  primary key (student_id, obstacle_id)
)