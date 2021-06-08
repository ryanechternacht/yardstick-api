create table pending_grant (
  email text not null,
  permission text not null,
  target_type text not null,
  target_id integer not null,
  constraint grant_target_type check (target_type in ('student')),
  constraint grant_permission check (permission in ('read', 'write'))
);
--;;
CREATE INDEX pending_grant_email 
ON pending_grant(email);
