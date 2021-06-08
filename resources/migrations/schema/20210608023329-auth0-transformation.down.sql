alter table yardstick_user drop column email;
--;;
alter table yardstick_user drop column picture;
--;;
alter table yardstick_user add column password_plaintext text not null;
--;;
alter table yardstick_user add column username text not null;
