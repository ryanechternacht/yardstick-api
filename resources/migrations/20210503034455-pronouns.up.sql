create table pronouns (
  id text primary key,
  nominative text not null,
  nominative_upper text not null,
  possessive text not null,
  possessive_upper text not null,
  accusative text not null,
  accusative_upper text not null
);
--;;
insert into pronouns values
('m', 'he', 'He', 'his', 'His', 'him', 'Him'),
('f', 'she', 'She', 'hers', 'Hers', 'her', 'Her'),
('t', 'they', 'They', 'theirs', 'Theirs', 'them', 'Them');
