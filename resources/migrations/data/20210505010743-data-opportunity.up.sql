insert into language_lookup (id, lang_en, lang_es) values
('opportunity-title-shooting-star', 'Shooting Star', 'Estrella fugaz'),
('opportunity-title-top-of-class', 'Top of Class', 'Top de su clase'),
('opportunity-title-math-wizard', 'Math Wizard', 'Asistente de matemáticas'),
('opportunity-description-shooting-star', 'Holy Moly! ${student.name.first} grew more than <span class="font-bold">88% of ${student.pronouns.possessive} peers</span> on the most recent ${assessment.name} Assessment', '¡Santo Moly! ${student.name.first} creció más que el <span class="font-bold">88% de ${student.pronouns.possessive} compañeros</span> en la evaluación ${assessment.name} más reciente'),
('opportunity-description-top-of-class', '${student.name.first} has the highest ${assessment.name} score in <span class="font-bold">The Real and Complex Number System</span> in ${student.pronouns.possessive} class. Is ice cream in order?', '${student.name.first} tiene la puntuación más alta de ${assessment.name} en <span class="font-bold">El sistema de números reales y complejos</span> en la clase ${student.pronouns.possessive}. ¿Está el helado en orden?'),
('opportunity-description-math-wizard', '${student.name.first} might have some magic up ${student.pronouns.possessive} sleeves as ${student.pronouns.nominative} is <span class="font-bold">Proficient</span> or <span class="font-bold">On Track</span> across all of ${student.pronouns.possessive} math assessments', '${student.name.first} podría tener algo de magia en las fundas de ${student.pronouns.possessive} ya que ${student.pronouns.nominative} es <span class="font-bold">competente</span> o <span class="font-bold">En camino</span> en todas las evaluaciones de matemáticas de ${student.pronouns.possessive}')
--;;
insert into opportunity (title_lang, image, description_lang) values
('opportunity-title-shooting-star', '/images/opportunities-star.png', 'opportunity-description-shooting-star'),
('opportunity-title-top-of-class', '/images/opportunities-award.png', 'opportunity-description-top-of-class'),
('opportunity-title-math-wizard', '/images/opportunities-wizard.png', 'opportunity-description-math-wizard')
--;;
insert into student_opportunity (student_id, opportunity_id) values
(1, 1), (1, 2), (1, 3)