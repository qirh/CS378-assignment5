
create cs378_almto3;
use cs378_almto3;

create table IF NOT EXISTS Projects(projectName varchar(255) NOT NULL, projectDescription varchar(255) NOT NULL, projectId int NOT NULL AUTO_INCREMENT, PRIMARY KEY(projectId));
create table IF NOT EXISTS Meetings(meetingName varchar(255) NOT NULL, meetingYear varchar(255) NOT NULL, meetingId int NOT NULL AUTO_INCREMENT, projectId int NOT NULL, PRIMARY KEY(meetingId), FOREIGN KEY (projectId) REFERENCES Projects(projectId) ON DELETE CASCADE);
              
insert into Projects(projectName, projectDescription, projectId) values("Data Management", "Data Managment course", 2);
insert into Projects(projectName, projectDescription, projectId) values("Modern Web Apps", "Dr. dev's class", 1);

insert into Meetings(meetingName, meetingYear, meetingId, projectId) values("Modern Web Apps", "2014", 1, 2);

select * from Projects;
select * from Meetings;