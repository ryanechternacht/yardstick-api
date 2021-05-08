insert into language_lookup (id, lang_en) values
('opportunity-title-shooting-star', 'Shooting Star'),
('opportunity-title-top-of-class', 'Top of Class'),
('opportunity-title-math-wizard', 'Math Wizard'),
('opportunity-description-shooting-star', 'Holy Moly! ${student.name.first} grew more than <span class="font-bold">88% of ${student.pronouns.possessive} peers</span> on the most recent ${assessment.name} Assessment'),
('opportunity-description-top-of-class', '${student.name.first} has the highest ${assessment.name} score in <span class="font-bold">The Real and Complex Number System</span> in ${student.pronouns.possessive} class. Is ice cream in order?'),
('opportunity-description-math-wizard', '${student.name.first} might have some magic up ${student.pronouns.possessive} sleeves as ${student.pronouns.nominative} is <span class="font-bold">Proficient</span> or <span class="font-bold">On Track</span> across all of ${student.pronouns.possessive} math assessments')
--;;
insert into opportunity (title_lang, image, description_lang) values
('opportunity-title-shooting-star', '/images/opportunities-star.png', 'opportunity-description-shooting-star'),
('opportunity-title-top-of-class', '/images/opportunities-award.png', 'opportunity-description-top-of-class'),
('opportunity-title-math-wizard', '/images/opportunities-wizard.png', 'opportunity-description-math-wizard')
--;;
insert into student_opportunity (student_id, opportunity_id) values
(1, 1), (1, 2), (1, 3)