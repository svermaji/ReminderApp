Insert into sv_users
   (USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, PHONE, BIRTH_DATE, CITY, ZIP, STATE, COUNTRY, EMAIL)
 Values
   (4, 'sv', 'c3Y=', 'Shailendra', 'Verma', 'M', '917312486131', '09-FEB-1978', 'Indore', '452009', 'MP', 'India', 'sverma@impetus.co.in');
Insert into sv_users
   (USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, COUNTRY, EMAIL)
 Values
   (26, 'saurabh', 'c2F1cmFiaA==', 'saurabh', 'jain', 'M', 'India', 'svjain@impetus.co.in');
Insert into sv_users
   (USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, COUNTRY, EMAIL)
 Values
   (27, 'smb', 'c21i', 'Sayed', 'Bahauddin', 'M', 'India', 'sm_bahauddin@yahoo.com');
Insert into sv_users
   (USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, ADDRESS, PHONE, BIRTH_DATE, CITY, ZIP, STATE, COUNTRY, EMAIL)
 Values
   (1, 'admin', 'c3Zlcm1h', 'Shailendra', 'Verma', 'M', 'Sudama Nagar', '919827072272', '09-Feb-1978', 'Indore', '452009', 'MP', 'India', 'sverma@impetus.co.in');
Insert into sv_users
   (USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, BIRTH_DATE, COUNTRY, EMAIL)
 Values
   (2, 'test', 'dGVzdA==', 'Test', 'User', 'F', '23-FEB-2007', 'Other', 'sverma@impetus.co.in');
Insert into sv_users
   (USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, COUNTRY, EMAIL)
 Values
   (25, 'chitra', 'c2FoYWk=', 'Chitra', 'Sahai', 'F', 'India', 'nicechildmoony@yahoo.com');
Insert into sv_users
   (USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, COUNTRY, EMAIL)
 Values
   (28, 'anandmohans', 'anVzdGZvcmZ1bg==', 'Anand', 'Shrivastava', 'M', 'India', 'anandmohans@gmail.com');
COMMIT;


------------

Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (184, 'sv', '30-NOV-1969', 'marriage-anniversary', 'Marriage Date of Chacha G and Chachi G (Chotey)', 'sverma@impetus.co.in', 'Kuber Chacha ji', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (185, 'sv', '04-OCT-1980', 'birth-date', 'Birth Date of Nalin Mehta', 'sverma@impetus.co.in', 'Nalin', 1, 1, '04-Oct-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (186, 'sv', '14-JUN-1979', 'birth-date', 'Birth Date of Jayant Mehta', 'sverma@impetus.co.in', 'Jayant', 1, 0, '14-Jun-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (187, 'sv', '03-JUL-1980', 'birth-date', 'Birth date of Sunil Ajmera', 'sunil.ajm@gmail.com', 'Sunil Ajmera', 1, 1, '03-Jul-2006');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (188, 'sv', '07-JUL-1990', 'birth-date', 'Birthdate of Poursh', 'sandeepg80@yahoo.com', 'Porush', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (189, 'sv', '03-AUG-2003', 'friend-ship-date', 'Happy Friend Ship day First sunday of AUGust', 'shailendravermag@yahoo.com, sabhinandan@yahoo.co.in, abhishek_techie@yahoo.com, sandeep_ccna@yahoo.co.in, naikashishm@yahoo.co.in, sandeepg80@yahoo.com, sunil.ajm@gmail.com, sbahauddin@impetus.co.in, gurjeet77@yahoo.com, prashantrathore@yahoo.com, amshrivastava@impetus.co.in, sandeep.das@impetus.co.in, anaik@impetus.co.in, sandeep_ccna@yahoo.co.in', 'Friend Ship day', 3, 1, '03-Aug-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (190, 'sv', '24-AUG-1952', 'birth-date', 'Birthdate of Ashok Jiyaji DCADM', 'sverma@impetus.co.in', 'Ashok GjaG', 1, 1, '24-Aug-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (191, 'sv', '25-AUG-2001', 'birth-date', 'Birth date of Manoj Son', 'sverma@impetus.co.in', 'Manoj Son', 1, 1, '24-Aug-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (1, 'admin', '24-JAN-2007', 'marriage-anniversary', 'test', 'sverma@impetus.co.in', 'test', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (2, 'admin', '22-JAN-2007', 'birth-date', 'test', 'sverma@impetus.co.in', 'test', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (3, 'admin', '25-JAN-2007', 'other-memorable-event', 'test', 'sverma@impetus.co.in,sverma@impetus.co.in', 'test', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (4, 'test', '28-MAY-2007', 'other-memorable-event', 'test', 'xyz@impetus.co.in', 'test', 1, 1, '28-May-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (224, 'chitra', '09-APR-2007', 'birth-date', 'Abhishek Singhals Bday', 'abhishek.singhal@impetus.co.in', 'Abhishek Singhals', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (192, 'sv', '17-OCT-1977', 'birth-date', 'Birthdate of Gurjeet', 'gurjeet77@yahoo.com', 'Gurjeet', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (255, 'anandmohans', '27-JUN-2007', 'other-memorable-event', 'test even for Shailus application', 'amshrivastava@impetus.co.in', 'Anand', 1, 1, '27-Jun-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (194, 'sv', '21-FEB-1980', 'birth-date', 'Birthday of Sumit Nahar', 'sverma@impetus.co.in', 'Sumit Nahar', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (195, 'sv', '14-OCT-1976', 'birth-date', 'Birthday of Kamlesh Jain', 'kamalesh_jain12@rediffmail.com', 'Kamlesh Jain', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (196, 'sv', '21-AUG-1972', 'birth-date', 'Birth date of Nitesh Soni husband of Nitu', 'sverma@impetus.co.in', 'Nitesh', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (197, 'sv', '04-JUN-1978', 'birth-date', 'Birthdate of Abhishek Soni Chotu', 'sverma@impetus.co.in', 'Chotu', 1, 0, '04-Jun-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (198, 'sv', '02-JUN-1995', 'birth-date', 'Birthdate of Hritik Verma Honey son of Big B', 'sverma@impetus.co.in', 'Honey', 1, 1, '02-Jun-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (199, 'sv', '23-AUG-1978', 'birth-date', 'Birthday of Prashant Rathore', 'prashantrathore@yahoo.com', 'Prashant Rathore', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (200, 'sv', '26-DEC-1985', 'birth-date', 'Birth date of Purvi Mehta', 'purvi_mehta26@yahoo.com', 'Purvi', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (201, 'sv', '30-APR-1985', 'birth-date', 'Birth date of Nitika Vyas TITO', 'sverma@impetus.co.in', 'Nitika', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (202, 'sv', '14-OCT-1986', 'birth-date', 'Birth date of Rachana Ghayal', 'sverma@impetus.co.in', 'Rachana Ghayal', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (203, 'sv', '19-FEB-2000', 'marriage-anniversary', 'Marriage Anniversary of Manoj Khetwani', 'sverma@impetus.co.in', 'Manoj Khetwani', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (256, 'anandmohans', '10-JUL-2007', 'birth-date', 'Amit Bariars Bday on July 10th', 'amshrivastava@impetus.co.in', 'Anand', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (205, 'sv', '10-JAN-2004', 'other-memorable-event', 'Tito saide YES to me', 'sverma@impetus.co.in', 'Nitika', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (172, 'sv', '02-JUL-1990', 'marriage-anniversary', 'Marriage Date of Saroj G and Sunil Verma', 'sverma@impetus.co.in', 'Saroj G and Sunil GjaG', 1, 1, '02-Jul-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (207, 'sv', '02-JUL-1980', 'birth-date', 'Birthdate of Shriniwas Kale', 'shinu_smart@yahoo.com', 'Shriniwas Kale', 1, 1, '02-Jul-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (208, 'sv', '07-SEP-2005', 'birth-date', 'Birthdate of Raghav Verma', 'sverma@impetus.co.in', 'Raghav Verma', 1, 1, '07-Sep-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (277, 'sv', '14-DEC-2006', 'other-memorable-event', 'Maa left us...', 'sverma@impetus.co.in', 'Maa', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (225, 'sv', '09-APR-1984', 'birth-date', 'Birth Date of Neha Daga', 'miss_daga@yahoo.com', 'Neha Daga', 1, 1, '09-APR-1984');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (170, 'sv', '02-FEB-1989', 'marriage-anniversary', 'Marriage Date of Pinki G and Ashok Verma', 'sverma@impetus.co.in', 'Pinki G and Ashok GjaG', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (154, 'sv', '08-SEP-1962', 'birth-date', 'Birth Date of Bharti Verma', 'sverma@impetus.co.in', 'Bharti G', 1, 1, '07-Sep-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (253, 'smb', '16-MAY-2007', 'other-memorable-event', 'A family function.', 'sbahauddin@impetus.co.in', 'smb', 1, 0);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (150, 'sv', '23-OCT-1933', 'birth-date', 'Birthdate of B.S. Verma (Papa)', 'sverma@impetus.co.in', 'Papa', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (151, 'sv', '01-AUG-1988', 'other-memorable-event', 'Papa left us...', 'sverma@impetus.co.in', 'Papa', 3, 1, '01-Aug-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (152, 'sv', '23-MAY-1958', 'birth-date', 'Birth Date of Santosh Verma', 'sverma@impetus.co.in', 'Santosh G', 1, 1, '23-May-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (153, 'sv', '06-SEP-1960', 'birth-date', 'Birth Date of Savita Verma (Pinki G)', 'sverma@impetus.co.in', 'Pinki G', 1, 1, '06-Sep-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (155, 'sv', '24-May-1965', 'birth-date', 'Birth Date of Saroj Verma', 'sverma@impetus.co.in', 'Saroj G', 1, 1, '24-May-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (156, 'sv', '27-May-1967', 'birth-date', 'Birth Date of Jyoti Verma (Ajju G)', 'sverma@impetus.co.in', 'Ajju G', 1, 1, '25-May-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (157, 'sv', '30-JUL-1992', 'other-memorable-event', 'Death Date of Jyoti Verma (Ajju G)', 'sverma@impetus.co.in', 'Ajju G', 1, 1, '30-Jul-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (158, 'sv', '09-FEB-1978', 'birth-date', 'Birth Date of Shailendra Verma', 'sverma@impetus.co.in', 'Me', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (159, 'sv', '27-NOV-1985', 'birth-date', 'Birth Date of Vishal Verma', 'sverma@impetus.co.in', 'Vishal', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (160, 'sv', '23-OCT-1989', 'birth-date', 'Birth Date of Megha Verma', 'sverma@impetus.co.in', 'Megha', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (161, 'sv', '09-MAR-1990', 'birth-date', 'Birth Date of Ananta Soni (Saloni)', 'sverma@impetus.co.in', 'Saloni', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (162, 'sv', '03-JUN-1991', 'birth-date', 'Birth Date of Gourav Verma', 'sverma@impetus.co.in', 'Gourav', 1, 1, '03-Jun-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (163, 'sv', '29-JUN-1991', 'birth-date', 'Birth Date of  Jyotsana Verma (Soni)', 'sverma@impetus.co.in', 'Soni', 1, 1, '29-Jun-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (164, 'sv', '18-JUN-1993', 'birth-date', 'Birth Date of  Antra Soni (Vini)', 'sverma@impetus.co.in', 'Vini', 1, 1, '18-Jun-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (165, 'sv', '05-DEC-1995', 'birth-date', 'Birth Date of Arti Verma (Panda)', 'sverma@impetus.co.in', 'Aana', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (166, 'sv', '27-APR-1998', 'birth-date', 'Birth Date of Aditi Verma (Bua)', 'sverma@impetus.co.in', 'Aditi', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (167, 'sv', '11-MAY-2000', 'birth-date', 'Birth Date of Astha Verma (Futur)', 'sverma@impetus.co.in', 'Astha', 1, 1, '11-May-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (168, 'sv', '29-APR-1957', 'marriage-anniversary', 'Marriage Date of Shyama Devi and Bhagwan Sahay Verma', 'sverma@impetus.co.in', 'Maa aur Papa', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (169, 'sv', '28-NOV-1982', 'marriage-anniversary', 'Marriage Date of Santosh G and Hemant Verma', 'sverma@impetus.co.in', 'Santosh G and Hemant GjaG', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (171, 'sv', '04-FEB-1989', 'marriage-anniversary', 'Marriage Date of Bharti G and Kailash Soni', 'sverma@impetus.co.in', 'Bharti G and Kailash GjaG', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (173, 'sv', '26-FEB-1989', 'marriage-anniversary', 'Marriage Date of Ajju G and Shekhar Mairh', 'sverma@impetus.co.in', 'Ajju G and Shekhar Gjag', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (174, 'sv', '08-FEB-1963', 'birth-date', 'Birth Date of Sunil Verma', 'sverma@impetus.co.in', 'Suni GjaG', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (175, 'sv', '10-FEB-1953', 'birth-date', 'Birth Date of Dinesh Verma', 'sverma@impetus.co.in', 'Dinesh BS', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (176, 'sv', '05-DEC-1978', 'birth-date', 'Birth Date of Manoj Khetwani', 'sverma@impetus.co.in', 'Manoj Khetwani', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (177, 'sv', '05-FEB-1979', 'birth-date', 'Birth Date of Vipul Jajodia', 'sverma@impetus.co.in', 'Vipul', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (178, 'sv', '15-APR-1980', 'birth-date', 'Birth Date of Sandeep Singh Gaur', 'sandeepg80@yahoo.com', 'Sandeep Gaur', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (179, 'sv', '20-JAN-1965', 'birth-date', 'Birth Date of Dharmendra Verma (Tunnu Bhaiya)', 'sverma@impetus.co.in', 'Big B', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (180, 'sv', '04-OCT-1971', 'birth-date', 'Birth Date of Devendra Verma (Kannu Bhaiya)', 'sverma@impetus.co.in', 'Kannu Bhaiya', 1, 1, '04-Oct-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE, EMAIL_SEND_DATE)
 Values
   (181, 'sv', '09-AUG-1973', 'birth-date', 'Birth Date of Yogendra Verma (Bobby Bhaiya)', 'sverma@impetus.co.in', 'Don Bhaiya', 1, 1, '06-Aug-2007');
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (182, 'sv', '30-NOV-1937', 'birth-date', 'Birth Date of R.P.Verma (Darbar)', 'sverma@impetus.co.in', 'Darbar', 1, 1);
Insert into sv_events
   (EVENT_ID, USER_NAME, EVENT_DATE, EVENT_TYPE, EVENT_DESC, RECEIVER_EMAIL, EVENT_USER, EMAIL_MODE, ACTIVE)
 Values
   (183, 'sv', '30-NOV-1964', 'marriage-anniversary', 'Marriage Date of Chacha G and Chachi G (Darbar)', 'sverma@impetus.co.in', 'Darbar', 1, 1);
COMMIT;
