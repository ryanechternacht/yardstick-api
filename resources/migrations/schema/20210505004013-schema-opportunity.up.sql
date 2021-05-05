create table opportunity (
  id serial primary key,
  title text not null,
  image text not null,
  description text not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now()
);
--;;
create table student_opportunity (
  student_id int references student(id),
  opportunity_id int references opportunity(id),
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now(),
  primary key (student_id, opportunity_id)
)