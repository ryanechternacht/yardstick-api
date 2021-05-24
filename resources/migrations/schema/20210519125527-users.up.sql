create table users (
  id serial primary key,
  username text not null,
  password_plaintext text not null,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now()
)
