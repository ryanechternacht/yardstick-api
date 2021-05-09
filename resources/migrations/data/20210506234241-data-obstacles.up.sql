-- TODO Fiction Comprehension should be in additional_fields

insert into language_lookup (id, lang_en) values
('obstacle-greatest-question', 'Where do ${student.name.possessive} academic skills have the greatest obstacles to overcome for ${student.pronouns.accusative} to be college and career ready by graduation?'),
('obstacle-greatest-answer', 'Based on ${student.name.possessive} scores on ${student.name.possessive} NWEA MAP and Forward Exam, we see ${student.pronouns.possessive} greatest obstacles to overcome as ${student.pronouns.possessive} performance with <span class="underline">Fiction Comprehension</span>.'),
('obstacle-grade-comparison-question', 'How does ${student.name.first} compare to other ${student.grade.ordinal} graders?'),
('obstacle-grade-comparison-answer', 'Based on ${student.pronouns.possessive} assessments, ${student.name.possessive} performance is more than one grade level behind on related Fiction Comprehension skills.'),
('obstacle-overcoming-question', 'Why is overcoming these obstacles important for ${student.name.first} in the next few years?'),
('obstacle-overcoming-answer', 'The instruction ${student.name.first} will receive in ${student.pronouns.possessive} High School English Language Arts courses will likely not focus a great deal on comprehension. <br><br> Often instruction at the high school level is focused on more deeply analyzing a text, with teachers and the curriculum assuming that students can understand the material at a basic level.'),
('obstacle-performance-question', 'Can you show me what ${student.name.possessive} performance looks like?'),
('obstacle-performance-answer', 'The passage on the left represents an approximate text ${student.name.first} could read and comprehend. On the right is a text of what an On Track ${student.grade.ordinal} grader reader could comprehend.'),
('obstacle-future-question', 'Why is this important in the long run?'),
('obstacle-future-answer', 'Research has shown a strong connection between literacy skills (fiction and nonfiction comprehension) and success in highly sought-ofter jobs. <br><br> Meaning the stronger ${student.name.possessive} literacy skills are, the more opportunities will be on the table when ${student.pronouns.nominative} picks out ${student.pronouns.possessive} future career.')
--;;
insert into obstacle (type, question_lang, answer_lang) values
(
  'SimpleObstacle', 
  'obstacle-greatest-question',
  'obstacle-greatest-answer'
),
(
  'SimpleObstacle', 
  'obstacle-grade-comparison-question', 
  'obstacle-grade-comparison-answer'
),
(
  'SimpleObstacle', 
  'obstacle-overcoming-question', 
  'obstacle-overcoming-answer'
),
(
  'ReadingPassageObstacle', 
  'obstacle-performance-question', 
  'obstacle-performance-answer'
),
(
  'SimpleObstacle', 
  'obstacle-future-question', 
  'obstacle-future-answer'
)
--;;
insert into student_obstacle (student_id, obstacle_id, ordering, additional_fields) values
(1, 1, 1, null),
(1, 2, 2, null),
(1, 3, 3, null),
(1, 4, 4, '{ "studentLevel": 6, "targetLevel": 8 }'),
(1, 5, 5, null)