create table school (
  id serial primary key,
  name text not null
  -- TODO more fields?
);
--;;
create table district (
  id serial primary key,
  name text not null
  -- TODO more fields?
);
--;;
create table student (
  id serial primary key,
  first_name text not null,
  last_name text not null,
  pronouns_id text not null references pronouns(id),
  grade_id text not null references grade(id),
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now(),
  school_id int not null references school(id)
);
