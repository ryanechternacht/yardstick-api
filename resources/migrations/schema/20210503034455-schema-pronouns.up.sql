create table pronouns (
  id text primary key,
  nominative_lang text not null references language_lookup(id),
  nominative_upper_lang text not null references language_lookup(id),
  possessive_lang text not null references language_lookup(id),
  possessive_upper_lang text not null references language_lookup(id),
  accusative_lang text not null references language_lookup(id),
  accusative_upper_lang text not null references language_lookup(id)
);
--;;
insert into language_lookup (id, lang_en, lang_es) values
('pronoun-he', 'he', 'él'),
('pronoun-He', 'He', 'Él'),
('pronoun-his', 'his', 'su'),
('pronoun-His', 'His', 'Su'),
('pronoun-him', 'him', 'él'),
('pronoun-Him', 'Him', 'Él'),
('pronoun-she', 'she', 'ella'),
('pronoun-She', 'She', 'Ella'),
('pronoun-hers', 'hers', 'suya'),
('pronoun-Hers', 'Hers', 'Suya'),
('pronoun-her', 'her', 'su'),
('pronoun-Her', 'Her', 'Su'),
('pronoun-they', 'they', ''),
('pronoun-They', 'They', ''),
('pronoun-theirs', 'theirs', ''),
('pronoun-Theirs', 'Theirs', ''),
('pronoun-them', 'them', ''),
('pronoun-Them', 'Them', '')
--;;
insert into pronouns values
('m', 'pronoun-he', 'pronoun-He', 'pronoun-his', 'pronoun-His', 'pronoun-him', 'pronoun-Him'),
('f', 'pronoun-she', 'pronoun-She', 'pronoun-hers', 'pronoun-Hers', 'pronoun-her', 'pronoun-Her'),
('t', 'pronoun-they', 'pronoun-They', 'pronoun-theirs', 'pronoun-Theirs', 'pronoun-them', 'pronoun-Them');
--;;