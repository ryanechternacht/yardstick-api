create table assessment_trait (
  id serial primary key,
  assessment_id int not null references assessment(id),
  title text not null,
  description text not null,
  icon text
)
