-- TODO Fiction Comprehension should be in additional_fields

insert into language_lookup (id, lang_en, lang_es) values
('obstacle-greatest-question', 'Where do ${student.name.possessive} academic skills have the greatest obstacles to overcome for ${student.pronouns.accusative} to be college and career ready by graduation?', '¿Dónde tienen las habilidades académicas de ${student.name.possessive} los mayores obstáculos que superar para que ${student.pronouns.accusative} esté listo para la universidad y la carrera profesional antes de la graduación?'),
('obstacle-greatest-answer', 'Based on ${student.name.possessive} scores on ${student.name.possessive} NWEA MAP and Forward Exam, we see ${student.pronouns.possessive} greatest obstacles to overcome as ${student.pronouns.possessive} performance with <span class="underline">Fiction Comprehension</span>.', 'Según los puntajes de ${student.name.possessive} en el NWEA MAP y el examen Forward de ${student.name.possessive}, vemos los obstáculos más grandes de ${student.pronouns.possessive} que superar como desempeño de ${student.pronouns.possessive} con <span class="underline"> Comprensión de ficción </span>.'),
('obstacle-grade-comparison-question', 'How does ${student.name.first} compare to other ${student.grade.ordinal} graders?', '¿Cómo se compara ${student.name.first} con otros estudiantes de ${student.grade.ordinal}?'),
('obstacle-grade-comparison-answer', 'Based on ${student.pronouns.possessive} assessments, ${student.name.possessive} performance is more than one grade level behind on related Fiction Comprehension skills.', 'Según las evaluaciones de ${student.pronouns.possessive}, el rendimiento de ${student.name.possessive} está más de un nivel de grado por detrás en las habilidades de comprensión de ficción relacionadas.'),
('obstacle-overcoming-question', 'Why is overcoming these obstacles important for ${student.name.first} in the next few years?', '¿Por qué es importante superar estos obstáculos para ${student.name.first} en los próximos años?'),
('obstacle-overcoming-answer', 'The instruction ${student.name.first} will receive in ${student.pronouns.possessive} High School English Language Arts courses will likely not focus a great deal on comprehension. <br><br> Often instruction at the high school level is focused on more deeply analyzing a text, with teachers and the curriculum assuming that students can understand the material at a basic level.', 'La instrucción que recibirá ${student.name.first} en los cursos de Artes del Lenguaje en Inglés de la escuela secundaria ${student.pronouns.possessive} probablemente no se centrará mucho en la comprensión. <br> <br> A menudo, la instrucción en la escuela secundaria se centra en analizar más profundamente un texto, y los maestros y el plan de estudios suponen que los estudiantes pueden comprender el material a un nivel básico.'),
('obstacle-performance-question', 'Can you show me what ${student.name.possessive} performance looks like?', '¿Puedes mostrarme cómo es el rendimiento de ${student.name.possessive}?'),
('obstacle-performance-answer', 'The passage on the left represents an approximate text ${student.name.first} could read and comprehend. On the right is a text of what an On Track ${student.grade.ordinal} grader reader could comprehend.', 'El pasaje de la izquierda representa un texto aproximado que ${student.name.first} podría leer y comprender. A la derecha hay un texto de lo que podría comprender un lector calificador de On Track ${student.grade.ordinal}.'),
('obstacle-future-question', 'Why is this important in the long run?', '¿Por qué es esto importante a largo plazo?'),
('obstacle-future-answer', 'Research has shown a strong connection between literacy skills (fiction and nonfiction comprehension) and success in highly sought-ofter jobs. <br><br> Meaning the stronger ${student.name.possessive} literacy skills are, the more opportunities will be on the table when ${student.pronouns.nominative} picks out ${student.pronouns.possessive} future career.', 'Las investigaciones han demostrado una fuerte conexión entre las habilidades de alfabetización (comprensión de ficción y no ficción) y el éxito en trabajos muy solicitados. <br> <br> Es decir, cuanto más fuertes sean las habilidades de alfabetización de ${student.name.possessive}, más oportunidades habrá sobre la mesa cuando ${student.pronouns.nominative} elija la carrera futura de $ {student.pronouns.possessive}.')
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