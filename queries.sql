/* SQL for SQL Server */
if exists (select * from dbo.sysobjects where id = object_id(N'dbo.sv_events') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table dbo.sv_events
GO

if exists (select * from dbo.sysobjects where id = object_id(N'dbo.sv_users') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table dbo.sv_users
GO

CREATE TABLE dbo.sv_events (
	event_id int IDENTITY (1, 1) NOT FOR REPLICATION  NOT NULL ,
	user_name varchar (30)  NOT NULL ,
	event_date varchar (11) NOT NULL ,
	event_type varchar (30)  NOT NULL ,
	event_desc varchar (150)  NOT NULL ,
	receiver_email varchar (200)  NOT NULL 
) ON PRIMARY
GO

CREATE TABLE dbo.sv_users (
	user_id int IDENTITY (1, 1) NOT FOR REPLICATION  NOT NULL ,
	user_name varchar (20)  NOT NULL ,
	password varchar (20)  NOT NULL ,
	first_name varchar (20)  NOT NULL ,
	last_name varchar (20)  NOT NULL ,
	gender varchar (1)  NOT NULL ,
	address varchar (70)  NULL ,
	phone varchar (30) NULL ,
	birth_date varchar (11) NULL ,
	city varchar (20)  NULL ,
	zip varchar (10) NULL ,
	state varchar (50)  NULL ,
	country varchar (20)  NULL ,
	email varchar (30)  NOT NULL 
) ON PRIMARY
GO

insert into sv_users values ('admin', 'c3Zlcm1h', 'Shailendra', 'Verma', 'M', 'Sudama Nagar', '919827072272', '09-Feb-1978', 'Indore', '452009', 'MP', 'India', 'shailendravermag@yahoo.com');
insert into sv_events values ('admin', '22-Jan-2007', 'birth-date', 'test', 'sverma@impetus.co.in');
insert into sv_events values ('admin', '24-Jan-2007', 'marriage-anniversary', 'test', 'sverma@impetus.co.in');
insert into sv_events values ('admin', '25-Jan-2007', 'other-memorable-event', 'test', 'sverma@impetus.co.in,sverma@impetus.co.in')

insert into sv_users values ('test', 'dGVzdA==', 'Test', 'User', 'M', '', '', '', '', '', '', 'India', 'test@yahoo.com');
insert into sv_events values ('test', '25-Jan-2007', 'other-memorable-event', 'test', 'test2@test.com')

/*
select *, 
(select email from sv_users u where u.user_name=ev.user_name) 'user_email'
, DATEDIFF(day, event_date, getdate()) 'date_diff'
from sv_events ev where DATEDIFF(day, event_date, getdate()) > -7 and DATEDIFF(day, event_date, getdate()) < 1
*/

----------------------------------------------------------------
/* SQL for Oracle */


drop table sv_events;
drop table sv_users;

CREATE TABLE SV_EVENTS
(
  EVENT_ID         NUMBER(4),
  USER_NAME        NVARCHAR2(30)                NOT NULL,
  EVENT_DATE       NVARCHAR2(11)                NOT NULL,
  EVENT_TYPE       NVARCHAR2(30)                NOT NULL,
  EVENT_DESC       NVARCHAR2(150)               NOT NULL,
  RECEIVER_EMAIL   NVARCHAR2(400)               NOT NULL,
  EVENT_USER       NVARCHAR2(30),
  EMAIL_MODE       NUMBER(1)                    DEFAULT NULL,
  ACTIVE           NUMBER(1)                    DEFAULT 1,
  EMAIL_SEND_DATE  NVARCHAR2(11)
)

CREATE TABLE sv_users (
	user_id NUMBER (4) PRIMARY KEY,
	user_name varchar (20)  NOT NULL ,
	password varchar (20)  NOT NULL ,
	first_name varchar (20)  NOT NULL ,
	last_name varchar (20)  NOT NULL ,
	gender varchar (1)  NOT NULL ,
	address varchar (70)  NULL ,
	phone varchar (30) NULL ,
	birth_date varchar (11) NULL ,
	city varchar (20)  NULL ,
	zip varchar (10) NULL ,
	state varchar (50)  NULL ,
	country varchar (20)  NULL ,
	email varchar (30)  NOT NULL 
);

DROP SEQUENCE SEQ_SV_USERS;

CREATE SEQUENCE SEQ_SV_USERS
  START WITH 1
  NOMAXVALUE
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


DROP SEQUENCE SEQ_SV_EVENTS;

CREATE SEQUENCE SEQ_SV_EVENTS
  START WITH 1
  NOMAXVALUE
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;



insert into sv_users values (SEQ_SV_USERS.NEXTVAL, 'admin', 'c3Zlcm1h', 'Shailendra', 'Verma', 'M', 'Sudama Nagar', '919827072272', '09-Feb-1978', 'Indore', '452009', 'MP', 'India', 'shailendravermag@yahoo.com');
insert into sv_users values (SEQ_SV_USERS.NEXTVAL, 'test', 'dGVzdA==', 'Test', 'User', 'M', '', '', '', '', '', '', 'India', 'test@yahoo.com');
insert into sv_users values (SEQ_SV_USERS.NEXTVAL, 'sv', 'c3Y=', 'Shailendra', 'Verma', 'M', 'Sudama Nagar', '919827072272', '09-Feb-1978', 'Indore', '452009', 'MP', 'India', 'shailendravermag@yahoo.com');

insert into sv_events values (SEQ_SV_EVENTS.NEXTVAL, 'admin', '24-JAN-2007', 'marriage-anniversary', 'test', 'sverma@impetus.co.in');
insert into sv_events values (SEQ_SV_EVENTS.NEXTVAL, 'admin', '22-JAN-2007', 'birth-date', 'test', 'sverma@impetus.co.in');
insert into sv_events values (SEQ_SV_EVENTS.NEXTVAL, 'admin', '25-JAN-2007', 'other-memorable-event', 'test', 'sverma@impetus.co.in,sverma@impetus.co.in');
insert into sv_events values (SEQ_SV_EVENTS.NEXTVAL, 'test', '25-JAN-2007', 'other-memorable-event', 'test', 'test2@test.com')

Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '09-APR-1984', 'birth-date', 'Birth Date of Neha Daga', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '23-OCT-1933', 'birth-date', 'Birthdate of B.S. Verma (Papa)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '01-AUG-1988', 'other-memorable-event', 'Death Date of B.S.Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '23-MAY-1957', 'birth-date', 'Birth Date of Santosh Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '06-SEP-1960', 'birth-date', 'Birth Date of Savita Verma (Pinki G)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '08-SEP-1962', 'birth-date', 'Birth Date of Bharti Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '24-May-1965', 'birth-date', 'Birth Date of Saroj Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '27-May-1967', 'birth-date', 'Birth Date of Jyoti Verma (Ajju G)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '30-JUL-1992', 'other-memorable-event', 'Death Date of Jyoti Verma (Ajju G)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '09-FEB-1978', 'birth-date', 'Birth Date of Shailendra Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '27-NOV-1985', 'birth-date', 'Birth Date of Vishal Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '23-OCT-1989', 'birth-date', 'Birth Date of Megha Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '09-MAR-1990', 'birth-date', 'Birth Date of Ananta Soni (Saloni)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '03-JUN-1991', 'birth-date', 'Birth Date of Gourav Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '29-JUN-1991', 'birth-date', 'Birth Date of  Jyotsana Verma (Soni)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '18-JUN-1993', 'birth-date', 'Birth Date of  Antra Soni (Vini)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '05-DEC-1995', 'birth-date', 'Birth Date of Arti Verma (Panda)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '27-APR-1998', 'birth-date', 'Birth Date of Aditi Verma (Bua)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '11-MAY-2000', 'birth-date', 'Birth Date of Astha Verma (Futur)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '29-APR-1956', 'marriage-anniversary', 'Marriage Date of Shyama Devi and Bhagwan Sahay Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '28-NOV-1982', 'marriage-anniversary', 'Marriage Date of Santosh G and Hemant Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '02-FEB-1989', 'marriage-anniversary', 'Marriage Date of Pinki G and Ashok Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '04-FEB-1989', 'marriage-anniversary', 'Marriage Date of Bharti G and Kailash Soni', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '02-JUL-1990', 'marriage-anniversary', 'Marriage Date of Saroj G and Sunil Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '26-FEB-1989', 'marriage-anniversary', 'Marriage Date of Ajju G and Shekhar Mairh', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '08-FEB-1963', 'birth-date', 'Birth Date of Sunil Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '10-FEB-1953', 'birth-date', 'Birth Date of Dinesh Verma', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '05-DEC-1978', 'birth-date', 'Birth Date of Manoj Khetwani', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '05-FEB-1979', 'birth-date', 'Birth Date of Vipul Jajodia', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '15-APR-1980', 'birth-date', 'Birth Date of Sandeep Singh Gaur', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '20-JAN-1965', 'birth-date', 'Birth Date of Dharmendra Verma (Tunnu Bhaiya)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '04-OCT-1971', 'birth-date', 'Birth Date of Devendra Verma (Kannu Bhaiya)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '09-AUG-1973', 'birth-date', 'Birth Date of Yogendra Verma (Bobby Bhaiya)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '30-NOV-1937', 'birth-date', 'Birth Date of R.P.Verma (Darbar)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '30-NOV-1964', 'marriage-anniversary', 'Marriage Date of Chacha G and Chachi G (Darbar)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '30-NOV-1969', 'marriage-anniversary', 'Marriage Date of Chacha G and Chachi G (Chotey)', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '04-OCT-1980', 'birth-date', 'Birth Date of Nalin Mehta', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '14-JUN-1979', 'birth-date', 'Birth Date of Jayant Mehta', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '03-JUL-1980', 'birth-date', 'Birth date of Sunil Ajmera', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '07-JUL-1990', 'birth-date', 'Birthdate of Poursh', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '03-AUG-2003', 'friend-ship-date', 'Happy Friend Ship day First sunday of AUGust', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '24-AUG-1952', 'birth-date', 'Birthdate of Ashok Jiyaji DCADM', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '25-AUG-2001', 'birth-date', 'Birth date of Manoj Son', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '17-OCT-1977', 'birth-date', 'Birthdate of Gurjeet', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '21-FEB-1980', 'birth-date', 'Birthday of Sumit Nahar', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '14-OCT-1976', 'birth-date', 'Birthday of Kamlesh Jain', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '21-AUG-1972', 'birth-date', 'Birth date of Nitesh Soni husband of Nitu', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '04-JUN-1978', 'birth-date', 'Birthdate of Abhishek Soni Chotu', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '02-JUN-1995', 'birth-date', 'Birthdate of Hritik Verma Honey son of Big B', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '23-AUG-1978', 'birth-date', 'Birthday of Prashant Rathore', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '26-DEC-1985', 'birth-date', 'Birth date of Purvi Mehta', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '30-APR-1985', 'birth-date', 'Birth date of Nitika Vyas TITO', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '14-OCT-1986', 'birth-date', 'Birth date of Rachana Ghayal', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '19-FEB-2000', 'marriage-anniversary', 'Marriage Anniversary of Manoj Khetwani', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '10-JAN-2004', 'other-memorable-event', 'Tito saide YES to me', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '02-JUL-1980', 'birth-date', 'Birthdate of Shriniwas Kale', 'sverma@impetus.co.in');
Insert into sv_events Values (SEQ_SV_EVENTS.NEXTVAL, 'sv', '07-SEP-2005', 'birth-date', 'Birthdate of Raghav Verma', 'sverma@impetus.co.in');
COMMIT;

