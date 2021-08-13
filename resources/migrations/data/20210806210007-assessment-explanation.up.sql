insert into assessment_trait (title, description, icon) values 
('Adaptive', '${assessment.name} responds to how your student is performing, getting harder or easier to gain a better understanding of their abilities.', '/images/adaptive-icon.svg'),
('Subject Based', '${student.name.first} takes one test per subject area. ${student.pronouns.possessiveUpper} most recent testing was in Reading and Math.', '/images/subject-based-icon.svg'),
('Reoccurring', '${student.name.first} takes the ${assessment.name} assessment 3-4 times per year. <br><br> Normally once in the in the fall, winter and spring.', '/images/reoccuring-icon.svg'),
('Normative', '${student.name.possessive} ${assessment.name} Scores can be easily compared to students in ${student.pronouns.possessive} grade level all across the country.', '/images/normative-icon.svg');
--;;
insert into assessment_assessment_trait (assessment_id, assessment_trait_id) values 
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 1), (2, 2), (2, 3), (2, 4),
(3, 1), (3, 2), (3, 3), (3, 4),
(4, 1), (4, 2), (4, 3), (4, 4);
