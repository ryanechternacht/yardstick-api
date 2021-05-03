create table student (
  id serial primary key,
  first_name text not null,
  last_name text not null,
  pronouns_id text not null references pronouns(id),
  grade_id text not null references grade(id),
  created_at timestamp without time zone default now(),
  updated_at timestamp without time zone default now()
)
