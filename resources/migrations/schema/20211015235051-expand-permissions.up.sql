alter table yardstick_grant
drop constraint grant_target_type;
--;;
alter table yardstick_grant
add constraint grant_target_type check (target_type in ('student', 'school'));
--;;

alter table yardstick_grant
drop constraint grant_permission;
--;;
alter table yardstick_grant
add constraint grant_permission check (permission in ('read', 'write', 'admin'));
