insert into opportunity (title, image, description) values
('Shooting Star', '/images/opportunities-star.png', 'Holy Moly! ${student.name.first} grew more than <span class=\"font-bold\">88% of ${student.pronouns.possessive} peers</span> on the most recent ${assessment.name} Assessment'),
('Top of Class', '/images/opportunities-award.png', '${student.name.first} has the highest ${assessment.name} score in <span class=\"font-bold\">The Real and Complex Number System</span> in ${student.pronouns.possessive} class. Is ice cream in order?'),
('Math Wizard', '/images/opportunities-wizard.png', '${student.name.first} might have some magic up ${student.pronouns.possessive} sleeves as ${student.pronouns.nominative} is <span class=\"font-bold\">Proficient</span> or <span class=\"font-bold\">On Track</span> across all of ${student.pronouns.possessive} math assessments')
--;;
insert into student_opportunity (student_id, opportunity_id) values
(1, 1), (1, 2), (1, 3)