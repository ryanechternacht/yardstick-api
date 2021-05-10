insert into language_lookup (id, lang_en, lang_es) values
(
  'support-gist-overview-title', 
  'Help ${student.name.first} Comprehend What ${student.pronouns.nominativeUpper} Is Reading By Having Gist Conversations',
  'Ayude a ${student.name.first} a comprender lo que está leyendo ${student.pronouns.nominativeUpper} teniendo conversaciones esenciales'
),
(
  'support-gist-overview-description', 
  '"Gist" is a great tool for helping readers develop a reoccurring habit of self monitoring. The goal of this action plan is for ${student.name.first} to not get stuck in the weeds with every vocabulary word but instead nudge ${student.pronouns.accusative} to be focusing on the main idea of what ${student.pronouns.nominative} has just read. By having quick, frequent conversations with ${student.name.first} you can help ${student.pronouns.accusative} develop this habit in just a few weeks.',
  '"Esencia" es una gran herramienta para ayudar a los lectores a desarrollar un hábito recurrente de autocontrol. El objetivo de este plan de acción es que ${student.name.first} no se quede atascado en la hierba con cada palabra de vocabulario, sino que empuje a ${student.pronouns.accusative} para que se centre en la idea principal de lo que ${student.pronouns.nominative} acaba de leerse. Al tener conversaciones rápidas y frecuentes con ${student.name.first}, puedes ayudar a ${student.pronouns.accusative} a desarrollar este hábito en solo unas pocas semanas.'
),
(
  'support-gist-overview-action', 
  'Dive into the Gist action plan',
  'Sumérjase en el plan de acción esencial'
),
(
  'support-gist-details-title', 
  'Gist Action Plan',
  'Plan de acción esencial'
),
(
  'support-gist-details-subtitle', 
  'Sometimes getting the gist is enough.',
  'A veces, tener la esencia es suficiente.'
),
(
  'support-gist-details-description', 
  'It is not always necessary for readers to understand every word.  Sometimes struggling readers slow down comprehension by grappling with each word.  This can be discouraging and stall momentum so that comprehension breaks down further.  Work with your child determine the gist or essential idea of a passage.',
  'No siempre es necesario que los lectores comprendan cada palabra. A veces, los lectores con dificultades ralentizan la comprensión al lidiar con cada palabra. Esto puede ser desalentador y estancar el impulso de modo que la comprensión se deteriore aún más. Trabaje con su hijo para determinar la esencia o la idea esencial de un pasaje.'
),
(
  'support-book-selection-overview-title',
  'Assist ${student.name.first} in Selecting Fiction Books That Match ${student.pronouns.possessiveUpper} Developmental Level',
  'Ayudar a ${student.name.first} a seleccionar libros de ficción que coincidan con el nivel de desarrollo de ${student.pronouns.possessiveUpper}'
),
(
  'support-book-selection-overview-description',
  'For ${student.pronouns.possessive} independent reading to have a positive impact on ${student.name.possessive} comprehension ${student.pronouns.nominative} needs to be engaging with books at the right level. Too easy, and ${student.pronouns.nominative} won’t be pushed to stretch ${student.pronouns.possessive} abilities. Too hard and it is likely to be a frustrating experience. Using online tools you can help ${student.pronouns.accusative} select books in ${student.pronouns.possessive} development sweetspot.',
  'Para que la lectura independiente de ${student.pronouns.possessive} tenga un impacto positivo en la comprensión de ${student.name.possessive}, ${student.pronouns.nominative} debe interactuar con los libros en el nivel correcto. Demasiado fácil, y ${student.pronouns.nominative} no se verá obligado a estirar las habilidades de ${student.pronouns.possessive}. Demasiado difícil y es probable que sea una experiencia frustrante. Con las herramientas en línea, puedes ayudar a ${student.pronouns.accusative} a seleccionar libros en el punto dulce del desarrollo de ${student.pronouns.possessive}.'
),
(
  'support-book-selection-overview-action',
  'View the Selecting Books action plan',
  'Ver el plan de acción Seleccionar libros'
),
(
  'support-vocabulary-overview-title',
  'Work With ${student.name.first} to Master More Grade Level Vocabulary',
  'Trabaje con ${student.name.first} para dominar más vocabulario de nivel de grado'
),
(
  'support-vocabulary-overview-description',
  'Knowing ${student.name.first} needs increased support to build out ${student.pronouns.possessive} understanding of grade level appropriate vocabulary helping ${student.name.accusative} work through a set of flashcards is a quick way to help ${student.pronouns.accusative} feel more comfortable when tackling challenging texts.',
  'Saber que ${student.name.first} necesita un mayor apoyo para desarrollar la comprensión de ${student.pronouns.possessive} del vocabulario apropiado para el nivel de grado, ayudar a ${student.name.accusative} a trabajar con un conjunto de tarjetas didácticas es una forma rápida de ayudar ${student.pronouns.accusative} se siente más cómodo al abordar textos desafiantes.'
),
(
  'support-vocabulary-overview-action',
  'Review the Grade Level Vocab action plan',
  'Revise el plan de acción de Vocab de nivel de grado'
)
--;;
insert into support (
  overview_title_lang, 
  overview_description_lang, 
  overview_action_lang, 
  details_title_lang, 
  details_subtitle_lang, 
  details_description_lang
) 
values
(
  'support-gist-overview-title', 
  'support-gist-overview-description',
  'support-gist-overview-action',
  'support-gist-details-title',
  'support-gist-details-subtitle',
  'support-gist-details-description' 
),
(
  'support-book-selection-overview-title',
  'support-book-selection-overview-description',
  'support-book-selection-overview-action',
  'support-gist-details-title',
  'support-gist-details-subtitle',
  'support-gist-details-description'
),
(
  'support-vocabulary-overview-title',
  'support-vocabulary-overview-description',
  'support-vocabulary-overview-action',
  'support-gist-details-title',
  'support-gist-details-subtitle',
  'support-gist-details-description'
)
--;;
insert into student_support (student_id, support_id, ordering) values
(1, 1, 1), (1, 2, 2), (1, 3, 3)
--;;
insert into language_lookup (id, lang_en, lang_es) values
('tag-school', 'Suggested by School', 'Sugerido por la escuela'),
('tag-daily', 'Daily', 'Diario'),
('tag-no-computer', 'Computer not Required', 'Computadora no requerida'),
('tag-teacher', 'Suggested by Teacher', 'Sugerido por el profesor'),
('tag-ongoing', 'Ongoing', 'En marcha'),
('tag-computer', 'Computer Based', 'Basado en computadora')
--;;
insert into support_tag (tag_lang) values
('tag-school'),
('tag-daily'),
('tag-no-computer'),
('tag-teacher'),
('tag-ongoing'),
('tag-computer')
--;;
insert into support_support_tag (support_id, support_tag_id, ordering) values
(1, 1, 1), (1, 2, 2), (1, 3, 3),
(2, 4, 1), (2, 5, 2), (2, 6, 3),
(3, 1, 1), (3, 2, 2), (3, 3, 3)
--;;
insert into language_lookup (id, lang_en, lang_es) values
('step-title-do-this', 'Do This', 'Hacer esto'),
('step-body-gist-do-this', 'Read with ${student.name.first} for at least 20 minutes and ask ${student.pronouns.accusative} to share the “Gist” of what ${student.pronouns.nominative} read with you', 'Lea con ${student.name.first} durante al menos 20 minutos y pídale a ${student.pronouns.accusative} que comparta la "esencia" de lo que ${student.pronouns.nominative} leyó con usted'),
('step-title-using-this', 'Using This', 'Usando esto'),
('step-body-gist-using-this', 'Model stating the gist using this <a href="https://nysrti.org/intervention-tools/reading-tools/tool:getthegist/">Reseach supported one-pager</a> that outlines an easy way to explain Gist.', 'Modele explicando lo esencial usando este <a href="https://nysrti.org/intervention-tools/reading-tools/tool:getthegist/"> Reseach compatible con una página</a> que describe una manera fácil de explicar lo esencial.'),
('step-title-this-often', 'This Often', 'Esto a menudo'),
('step-body-this-often', 'Read with ${student.name.first} and have ${student.pronouns.accusative} write or say the Gist 3-4 times a week', 'Lea con ${student.name.first} y haga que ${student.pronouns.accusative} escriba o diga lo esencial de 3 a 4 veces a la semana.'),
('step-title-for-this-long', 'For This Long', 'Por este largo'),
('step-body-for-this-long', 'Keep this up for 4 weeks or until you start to see ${student.name.possessive} ability to accurately share the Gist of what ${student.pronouns.nominative} read improve', 'Continúe así durante 4 semanas o hasta que comience a ver la capacidad de ${student.name.possessive} para compartir con precisión la esencia de lo que ${student.pronouns.nominative} lee mejorar')
--;;
insert into support_step (title_lang, step_lang) values
('step-title-do-this', 'step-body-gist-do-this'),
('step-title-using-this', 'step-body-gist-using-this'),
('step-title-this-often', 'step-body-this-often'),
('step-title-for-this-long', 'step-body-for-this-long')
--;;
insert into support_support_step (support_id, support_step_id, ordering) values
(1, 1, 1), (1, 2, 2), (1, 3, 3), (1, 4, 4),
(2, 1, 1), (2, 2, 2), (2, 3, 3), (2, 4, 4),
(3, 1, 1), (3, 2, 2), (3, 3, 3), (3, 4, 4)