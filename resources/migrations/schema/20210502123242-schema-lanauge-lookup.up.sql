create table language_lookup (
  id text primary key,
  lang_en text not null default '',
  lang_es text not null default '',
  comment text,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now()
);