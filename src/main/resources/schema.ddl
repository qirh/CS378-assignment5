
create cs378_almto3;
use cs378_almto3;

create table IF NOT EXISTS Projects(name varchar(255) NOT NULL, des varchar(255) NOT NULL, id int NOT NULL AUTO_INCREMENT, PRIMARY KEY(id));

insert into Projects(name, des, id) values("Data Management", "Data Managment course", 2);
insert into Projects(name, des, id) values("Modern Web Apps", "Dr. dev's class", 1);

select * from Projects;
