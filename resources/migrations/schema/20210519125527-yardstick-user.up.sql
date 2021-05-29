create table yardstick_user (
  id serial primary key,
  username text not null,
  password_plaintext text not null,
  first_name text,
  last_name text,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now()
)
