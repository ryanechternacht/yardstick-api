alter table yardstick_user add column email text not null;
--;;
alter table yardstick_user drop column password_plaintext;
--;;
alter table yardstick_user drop column username;
