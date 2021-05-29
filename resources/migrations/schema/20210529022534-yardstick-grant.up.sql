create table yardstick_grant (
  id serial primary key,
  user_id int not null references yardstick_user(id),
  permission text not null,
  target_type text not null,
  target_id integer not null,
  constraint grant_target_type check (target_type in ('student')),
  constraint grant_permission check (permission in ('read', 'write'))
);