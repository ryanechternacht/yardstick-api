insert into obstacle (type, question, answer) values
(
  'SimpleObstacle', 
  'Where do ${student.name.possessive} academic skills have the greatest obstacles to overcome for ${student.pronouns.accusative} to be college and career ready by graduation?', 
  'Based on ${student.name.possessive} scores on ${student.name.possessive} NWEA MAP and Forward Exam, we see ${student.pronouns.possessive} greatest obstacles to overcome as ${student.pronouns.possessive} performance with <span class=\"underline\">Fiction Comprehension</span>.'
),
(
  'SimpleObstacle', 
  'How does ${student.name.first} compare to other ${student.grade.ordinal} graders?', 
  'Based on ${student.pronouns.possessive} assessments, ${student.name.possessive} performance is more than one grade level behind on related Fiction Comprehension skills.'
),
(
  'SimpleObstacle', 
  'Why is overcoming these osbstacles important for ${student.name.first} in the next few years?', 
  'The instruction ${student.name.first} will receive in ${student.pronouns.possessive} High School English Language Arts courses will likely not focus a great deal on comprehension. <br><br> Often instruction at the high school level is focused on more deeply analyzing a text, with teachers and the curriculum assuming that students can understand the material at a basic level.'
),
(
  'ReadingPassageObstacle', 
  'Can you show me what ${student.name.possessive} performance looks like?', 
  'The passage on the left represents an approximate text ${student.name.first} could read and comprehend. On the right is a text of what an On Track ${student.grade.ordinal} grader reader could comprehend.'
),
(
  'SimpleObstacle', 
  'Why is this important in the long run?', 
  'Research has shown a strong connection between literacy skills (fiction and nonfiction comprehension) and success in highly sought-ofter jobs. <br><br> Meaning the stronger ${student.name.possessive} literacy skills are, the more opportunities will be on the table when ${student.pronouns.nominative} picks out ${student.pronouns.possessive} future career.'
)
--;;
insert into student_obstacle (student_id, obstacle_id, ordering, additional_fields) values
(1, 1, 1, null),
(1, 2, 2, null),
(1, 3, 3, null),
(1, 4, 4, '{ "studentLevel": 6, "targetLevel": 8 }'),
(1, 5, 5, null)