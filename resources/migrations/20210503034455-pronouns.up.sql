create table pronouns (
  id text primary key,
  nominative text not null,
  nominativeUpper text not null,
  possessive text not null,
  possessiveUpper text not null,
  accusative text not null,
  accusativeUpper text not null
);
--;;
insert into pronouns values
('m', 'he', 'He', 'his', 'His', 'him', 'Him'),
('f', 'she', 'She', 'hers', 'Hers', 'her', 'Her'),
('t', 'they', 'They', 'theirs', 'Theirs', 'them', 'Them');
