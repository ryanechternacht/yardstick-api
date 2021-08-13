create table assessment_trait (
  id serial primary key,
  title text not null,
  description text not null,
  icon text
);
--;;
create table assessment_assessment_trait (
  id serial primary key,
  assessment_id int not null references assessment(id),
  assessment_trait_id int not null references assessment_trait(id)
);
