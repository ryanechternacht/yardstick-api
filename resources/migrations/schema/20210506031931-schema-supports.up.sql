create table support (
  id serial primary key,
  overview_title_lang text not null,
  overview_description_lang text not null,
  overview_action_lang text not null,
  details_title_lang text not null,
  details_subtitle_lang text not null,
  details_description_lang text not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now()
)
--;;
create table student_support (
  student_id int references student(id),
  support_id int references support(id),
  ordering int not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now(),
  primary key (student_id, support_id)
)
--;;
create table support_tag (
  id serial primary key,
  tag_lang text not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now()
)
--;;
create table support_support_tag (
  support_id int references support(id),
  support_tag_id int references support_tag(id),
  ordering int not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now(),
  primary key (support_id, support_tag_id)
)
--;;
create table support_step (
  id serial primary key,
  title_lang text not null,
  step_lang text not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now()
)
--;;
create table support_support_step (
  support_id int references support(id),
  support_step_id int references support_step(id),
  ordering int not null,
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now(),
  primary key (support_id, support_step_id)
)
