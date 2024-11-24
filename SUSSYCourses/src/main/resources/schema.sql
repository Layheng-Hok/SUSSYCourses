BEGIN;

DROP TABLE IF EXISTS courseware_student CASCADE;
DROP TABLE IF EXISTS course_student CASCADE;
DROP TABLE IF EXISTS course_courseware CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS courseware CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS web_app_user CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE role (
role_id SERIAL PRIMARY KEY,
role_name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE web_app_user (
user_id BIGSERIAL PRIMARY KEY,
full_name VARCHAR(50) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
role_id INT NOT NULL REFERENCES role(role_id),
enabled BOOLEAN NOT NULL,
verification_token VARCHAR(255),
gender VARCHAR(20) NOT NULL,
bio VARCHAR(300),
profile_picture VARCHAR(255),
points INT DEFAULT 0,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course (
course_id BIGSERIAL PRIMARY KEY,
course_name VARCHAR(100) NOT NULL,
description VARCHAR(300),
teacher_id BIGINT NOT NULL REFERENCES web_app_user(user_id),
type VARCHAR(20) NOT NULL CHECK (type IN ('open', 'non-open', 'semi-open')),
status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'approved', 'rejected')),
cover_image VARCHAR(255),
topic VARCHAR(50) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE courseware (
courseware_id BIGSERIAL PRIMARY KEY,
course_id BIGINT REFERENCES course(course_id),
file_type VARCHAR(10) NOT NULL CHECK (file_type IN ('md', 'pdf', 'mp4', 'pptx')),
category VARCHAR(20) NOT NULL CHECK (category IN ('lecture', 'assignment', 'project')),
url VARCHAR(255) NOT NULL UNIQUE,
downloadable BOOLEAN NOT NULL,
chapter INT NOT NULL,
courseware_order INT NOT NULL,
variant_of BIGINT,
version INT NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_student(
id BIGSERIAL PRIMARY KEY,
course_id BIGINT REFERENCES course(course_id),
student_id BIGINT REFERENCES web_app_user(user_id),
status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'enrolled', 'rejected')),
liked BOOLEAN NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
UNIQUE (course_id, student_id)
);

CREATE TABLE courseware_student(
courseware_id BIGINT REFERENCES courseware(courseware_id),
student_id BIGINT REFERENCES web_app_user(user_id),
completed BOOLEAN NOT NULL
);

CREATE TABLE comment(
comment_id BIGSERIAL PRIMARY KEY,
user_id BIGINT REFERENCES web_app_user(user_id),
course_id BIGINT REFERENCES course(course_id),
reply VARCHAR(300),
reply_id BIGINT,
message VARCHAR(300) NOT NULL,
created_at TIMESTAMP
);

CREATE TABLE rating(
rating_id BIGSERIAL PRIMARY KEY,
student_id BIGINT REFERENCES web_app_user(user_id) NOT NULL,
course_id BIGINT REFERENCES course(course_id) NOT NULL,
overall_rating FLOAT NOT NULL,
content_quality FLOAT NOT NULL,
teaching_competence FLOAT NOT NULL,
workload_balance FLOAT NOT NULL,
feedback VARCHAR(300),
created_at TIMESTAMP,
UNIQUE (student_id, course_id)
);

CREATE TABLE notification(
notification_id BIGSERIAL PRIMARY KEY,
sender_email VARCHAR(255) REFERENCES web_app_user(email) NOT NULL,
receiver_email VARCHAR(255) REFERENCES web_app_user(email) NOT NULL,
subject VARCHAR(100) NOT NULL,
text VARCHAR(500) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO role (role_name) VALUES ('ADMIN');
INSERT INTO role (role_name) VALUES ('STUDENT');
INSERT INTO role (role_name) VALUES ('INSTRUCTOR');
INSERT INTO role (role_name) VALUES ('PUBLIC');

INSERT INTO web_app_user (
full_name, email, password, role_id, enabled, verification_token, gender, bio, points, created_at, profile_picture
) VALUES
('Public User', 'sussycourses@gmail.com', '$2a$10$O2X7nLyPmmGr5EmTRAK5G./e3x1pkneX1/v4emOeBnSOj2Buv9iK.', 4, true, 'ce4103d6-de74-4d64-ac0a-4dee043ff67c', 'Other', NULL, 0, '2024-11-17 09:16:28.935239', null),
('Xing', 'henglayhok@gmail.com', '$2a$10$bwNu4hxiylq6RnEljauPBuC5rFrbXIQpxKdXDa/SFO/aYctWRGK5K', 1, true, '87c051f7-5613-49ab-968c-99921a77c612', 'Male', NULL, 0, '2024-11-17 09:16:28.935239', null),
('L Lawliet', 'hoklayheng33@gmail.com', '$2a$10$znvpPc2Doq4EmyOC8PgeAOCjqpA/X.lHbqunjXvHVywEWJOpa7Y8.', 2, true, '9cfc5093-02cd-4e64-8b72-560405aa72d1', 'Male', 'A brilliant detective with eccentric habits and unmatched deductive reasoning.', 0, '2024-11-17 09:16:28.935239', '300px-Ichimaru_Gin'),
('Kakashi Hatake', 'fbringer99@gmail.com', '$2a$10$HHzqkBErJo3jrrc9Zy/zRuiM0xdkz33CY4b4NR40bgvrZrhYANIiK', 3, true, 'a3c8731a-1c73-4753-ae5e-ba2e1e1698b1', 'Male', 'An elite ninja known for his Sharingan and relaxed but skilled teaching style.', 0, '2024-11-17 09:16:28.935239', null),
('Gin Ichimaru', 'aarontan113388@gmail.com', '$2a$10$hIe4TvNph/KXR0uwh/DGJ.LAtcV/mYe5eQv6GhLQP.gNad5AhZ/sm', 2, true, '00b32a7b-3775-4371-99c4-195a85697784', 'Male', 'A sly and enigmatic figure with a perpetually smiling face and hidden motives.', 0, '2024-11-17 20:14:27.229069',null),
('Grimmjow Jaegerjaquez', '12212027@mail.sustech.edu.cn', '$2a$10$2kr8dhrAGhOaXdnV60fvM.EbnxZ2GCC.K1BDzfGPEdSB5rIFrp6ae', 2, true, '981bcafe-42fd-4978-9d0d-fe570dcf4f6f', 'Male', 'A fierce and impulsive warrior with a passion for battle and an unyielding will.', 0, '2024-11-17 20:18:02.915353', null),
('Askin Nakk Le Vaar', 'lhok1@paragoniu.edu.kh', '$2a$10$vZW095fH5KT7EBmq/40FhOqjFCt8GShhlPKFroETGGZFXdNvYJ0OK', 3, true, 'f9a9fc2b-1781-49c0-b1ef-56c27195d775', 'Other', 'A laid-back yet deadly combatant known for his analytical mind and quirky personality.', 0, '2024-11-17 20:22:36.626827', null),
('Kisuke Urahara', '12210736@mail.sustech.edu.cn', '$2a$10$oV2TCELdlKRPQ8lW8Jovk.K87QMFdlHGefVCZsTp5nmmZ.JoRWxjW', 3, true, '4ef2873e-f1c5-4588-9466-eaabb3bb0a18', 'Male', 'A genius inventor and strategist with a carefree demeanor and a mysterious past.', 0, '2024-11-17 20:24:48.799666', null);


INSERT INTO course ( course_name, description, teacher_id, type, status, topic,created_at) VALUES
   ('Course1', 'blank', 4, 'open', 'approved', 'Programming', NOW()),
   ('Data Management', 'blank.', 8, 'open', 'approved', 'Data Science',NOW()),
   ('Spring Boot', 'blank', 4, 'semi-open', 'pending','Web Development',NOW()),
   ('Learn Axios', 'blank', 8, 'open', 'approved', 'Web Development',NOW()),
   ('Connecting frontend to backend', 'blank', 7, 'non-open', 'approved', 'Web Development',NOW()),
   ('Intro to web development', 'blank', 4, 'open', 'approved', 'Web Development',NOW()),
   ('How to sell a book', 'blank', 4, 'open', 'approved', 'Marketing',NOW()),
   ('Interior Design', 'blank', 7, 'open', 'approved',  'Design',NOW()),
   ('FPGA changes your life', 'blank', 4, 'open', 'approved', 'Hardware',NOW()),
   ('Tips to become a billionaire', 'blank', 4, 'open', 'rejected',  'Finance',NOW()),
   ('Learn about Inflation', 'blank', 7, 'open', 'approved', 'Economics',NOW() ),
   ('Lead your followers', 'blank', 8, 'open', 'pending', 'Leadership',NOW()),
   ('A million dollar business is not a dream', 'blank', 4, 'semi-open', 'approved','Entrepreneurship', NOW());


INSERT INTO courseware (course_id, file_type, category, url, downloadable, chapter, courseware_order,variant_of, version, created_at) VALUES
(1,'mp4', 'lecture', 'chapter1', FALSE, 1,1, 1, 1, NOW()),
(1,'pdf', 'lecture', 'intro_python.pdf', FALSE, 1,2, 2, 1,NOW()),
(1,'mp4', 'lecture', 'advanced_js.mp4', FALSE, 1,3, 3, 1,NOW()),
(2,'md', 'assignment', 'datascience_overview.md', FALSE, 1,1, 4, 1,NOW()),
(1, 'mp4', 'lecture', 'chapter1_version2', FALSE, 1, 1, 1, 2, NOW());

INSERT INTO course_student (course_id, student_id, status, liked)
VALUES
(1, 3, 'enrolled', TRUE),
(2, 3, 'pending', FALSE),
(4, 3, 'enrolled', TRUE),
(2, 6, 'enrolled', TRUE),
(4, 5, 'enrolled', TRUE),
(6, 3, 'enrolled', FALSE),
(8, 6, 'enrolled', TRUE),
(9, 5, 'enrolled', FALSE),
(7, 6, 'enrolled', TRUE),
(11, 5, 'enrolled', TRUE),
(8, 3, 'enrolled', FALSE),
(9, 6, 'enrolled', TRUE),
(2, 5, 'enrolled', FALSE),
(6, 5, 'enrolled', TRUE),
(11, 3, 'enrolled', TRUE);

INSERT INTO comment (user_id, course_id, reply, message, created_at) VALUES
(1, 1, NULL, 'Awesome site, awesome course, what else can I say?', '2023-10-01T12:00:00Z'),
(2, 1, NULL, 'This course is great!', '2023-10-02T09:30:00Z'),
(1, 1, NULL, 'I learned so much from this course.', '2023-10-03T14:15:00Z'),
(2, 1, NULL, 'Does anyone have suggestions for further reading?', '2023-10-04T16:45:00Z'),
(1, 1, NULL, 'The instructor explains concepts very clearly.', '2023-10-05T10:00:00Z'),
(2, 1, NULL, 'Can someone help me with chapter 2?', '2023-10-06T11:30:00Z'),
(1, 1, NULL, 'I found the assignments challenging but rewarding.', '2023-10-07T13:45:00Z'),
(2, 1, NULL, 'Are there any live sessions for this course?', '2023-10-08T15:20:00Z'),
(1, 1, NULL, 'Thank you for this amazing course!', '2023-10-09T17:10:00Z'),
(2, 1, NULL, 'Looking forward to taking more courses like this.', '2023-10-10T18:25:00Z');

INSERT INTO rating (rating_id ,course_id, student_id,overall_rating, content_quality, teaching_competence, workload_balance, feedback, created_at)
VALUES
(1,2, 6,4.0, 4.5, 4.0, 3.5, 'Good content, clear explanations.', NOW()),
(2,4, 5,3.5, 3.5, 4.0, 3.0, 'Fairly balanced, needs more examples.', NOW()),
(3,6, 3,5.0, 5.0, 5.0, 5.0, 'Exceptional course, highly recommend.', NOW()),
(4,8, 6,4.5, 4.5, 4.5, 4.5, 'Engaging and insightful.', NOW()),
(5,9,6 ,3.0, 2.5, 3.5, 3.0, 'A bit basic, but helpful.', NOW()),
(6,9, 5,4.0, 4.0, 4.0, 4.0, 'Solid course with practical examples.', NOW()),
(7,7, 6,4.5, 4.5, 5.0, 4.0, 'Instructor was very competent.', NOW()),
(8,11, 5,3.5, 3.5, 3.0, 4.0, 'Content is good, but workload is high.', NOW()),
(9,8, 3,4.0, 4.5, 3.5, 4.0, 'Good structure, some room for improvement.', NOW()),
(10,2, 5,3.0, 3.0, 3.5, 2.5, 'Average experience overall.', NOW()),
(11,6, 5,4.5, 4.5, 4.5, 4.5, 'Interesting insights, well-structured.', NOW()),
(12,11,3 ,3.5, 3.0, 4.0, 3.5, 'Good content but needs better pacing.', NOW());


END;