insert into support (
  overview_title, 
  overview_description, 
  overview_action, 
  details_title, 
  details_subtitle, 
  details_description
) 
values
(
  'Help ${student.name.first} Comprehend What ${student.pronouns.nominativeUpper} Is Reading By Having Gist Conversations', 
  '"Gist" is a great tool for helping readers develop a reoccurring habit of self monitoring. The goal of this action plan is for ${student.name.first} to not get stuck in the weeds with every vocabulary word but instead nudge ${student.pronouns.accusative} to be focusing on the main idea of what ${student.pronouns.nominative} has just read. By having quick, frequent conversations with ${student.name.first} you can help ${student.pronouns.accusative} develop this habit in just a few weeks.',
  'Dive into the Gist action plan',
  'Gist Action Plan',
  'Sometimes getting the gist is enough.',
  'It is not always necessary for readers to understand every word.  Sometimes struggling readers slow down comprehension by grappling with each word.  This can be discouraging and stall momentum so that comprehension breaks down further.  Work with your child determine the gist or essential idea of a passage.' 
),
(
  'Assist ${student.name.first} in Selecting Fiction Books That Match ${student.pronouns.possessiveUpper} Developmental Level',
  'For ${student.pronouns.possessive} independent reading to have a positive impact on ${student.name.possessive} comprehension ${student.pronouns.nominative} needs to be engaging with books at the right level. Too easy, and ${student.pronouns.nominative} won’t be pushed to stretch ${student.pronouns.possessive} abilities. Too hard and it is likely to be a frustrating experience. Using online tools you can help ${student.pronouns.accusative} select books in ${student.pronouns.possessive} development sweetspot.',
  'View the Selecting Books action plan',
  'Gist Action Plan',
  'Sometimes getting the gist is enough.',
  'It is not always necessary for readers to understand every word.  Sometimes struggling readers slow down comprehension by grappling with each word.  This can be discouraging and stall momentum so that comprehension breaks down further.  Work with your child determine the gist or essential idea of a passage.' 
),
(
  'Work With ${student.name.first} to Master More Grade Level Vocabulary',
  'Knowing ${student.name.first} needs increased support to build out ${student.pronouns.possessive} understanding of grade level appropriate vocabulary helping ${student.name.accusative} work through a set of flashcards is a quick way to help ${student.pronouns.accusative} feel more comfortable when tackling challenging texts.',
  'Review the Grade Level Vocab action plan',
  'Gist Action Plan',
  'Sometimes getting the gist is enough.',
  'It is not always necessary for readers to understand every word.  Sometimes struggling readers slow down comprehension by grappling with each word.  This can be discouraging and stall momentum so that comprehension breaks down further.  Work with your child determine the gist or essential idea of a passage.' 
)
--;;
insert into student_support (student_id, support_id, ordering) values
(1, 1, 1), (1, 2, 2), (1, 3, 3)
--;;
insert into support_tag (tag) values
('Suggested by School'),
('Daily'),
('Computer not Required'),
('Suggested by Teacher'),
('Ongoing'),
('Computer Based')
--;;
insert into support_support_tag (support_id, support_tag_id, ordering) values
(1, 1, 1), (1, 2, 2), (1, 3, 3),
(2, 4, 1), (2, 5, 2), (2, 6, 3),
(3, 1, 1), (3, 2, 2), (3, 3, 3)
--;;
insert into support_step (title, step) values
('Do This', 'Read with ${student.name.first} for at least 20 minutes and ask ${student.pronouns.accusative} to share the “Gist” of what ${student.pronouns.nominative} read with you'),
('Using This', 'Model stating the gist using this <a href="https://nysrti.org/intervention-tools/reading-tools/tool:getthegist/">Reseach supported one-pager</a> that outlines an easy way to explain Gist.'),
('This Often', 'Read with ${student.name.first} and have ${student.pronouns.accusative} write or say the Gist 3-4 times a week'),
('For This Long', 'Keep this up for 4 weeks or until you start to see ${student.name.possessive} ability to accurately share the Gist of what ${student.pronouns.nominative} read improve')
--;;
insert into support_support_step (support_id, support_step_id, ordering) values
(1, 1, 1), (1, 2, 2), (1, 3, 3), (1, 4, 4),
(2, 1, 1), (2, 2, 2), (2, 3, 3), (2, 4, 4),
(3, 1, 1), (3, 2, 2), (3, 3, 3), (3, 4, 4)