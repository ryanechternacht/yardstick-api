alter table yardstick_grant
drop constraint if exists grant_target_type;
--;;
alter table yardstick_grant
add constraint grant_target_type check (target_type in ('student'));
--;;

alter table yardstick_grant
drop constraint if exists grant_permission;
--;;
alter table yardstick_grant
add constraint grant_permission check (permission in ('read', 'write'));
