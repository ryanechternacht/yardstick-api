create table grade (
  id text primary key,
  cardinal text not null,
  ordinal text not null
)
--;;
insert into grade values
('p', 'Pre-K', 'Pre-K'),
('k', 'Kindergarten', 'Kindergarten'),
('1', '1', '1st'),
('2', '2', '2nd'),
('3', '3', '3rd'),
('4', '4', '4th'),
('5', '5', '5th'),
('6', '6', '6th'),
('7', '7', '7th'),
('8', '8', '8th'),
('9', '9', '9th'),
('10', '10', '10th'),
('11', '11', '11th'),
('12', '12', '12th')
