BEGIN;

DROP TABLE IF EXISTS courseware_student CASCADE;
DROP TABLE IF EXISTS course_student CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS courseware CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS stream CASCADE;
DROP TABLE IF EXISTS web_app_user CASCADE;
DROP TABLE IF EXISTS role CASCADE;

CREATE TABLE role
(
    role_id   SERIAL PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE web_app_user
(
    user_id            BIGSERIAL PRIMARY KEY,
    full_name          VARCHAR(50)  NOT NULL,
    email              VARCHAR(255) NOT NULL UNIQUE,
    password           VARCHAR(255) NOT NULL,
    role_id            INT          NOT NULL REFERENCES role (role_id),
    enabled            BOOLEAN      NOT NULL,
    verification_token VARCHAR(255),
    gender             VARCHAR(20)  NOT NULL,
    bio                VARCHAR(300),
    profile_picture    VARCHAR(255),
    points             INT       DEFAULT 0,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course
(
    course_id              BIGSERIAL PRIMARY KEY,
    course_name            VARCHAR(100) NOT NULL,
    description            VARCHAR(300),
    teacher_id             BIGINT       NOT NULL REFERENCES web_app_user (user_id),
    type                   VARCHAR(20)  NOT NULL CHECK (type IN ('open', 'non-open', 'semi-open')),
    status                 VARCHAR(20)  NOT NULL CHECK (status IN ('pending', 'approved', 'rejected')),
    cover_image            VARCHAR(255),
    topic                  VARCHAR(50)  NOT NULL,
    total_evaluation_score FLOAT        NOT NULL,
    num_evaluations        INTEGER      NOT NULL,
    like_count             BIGINT       NOT NULL,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE courseware
(
    courseware_id    BIGSERIAL PRIMARY KEY,
    course_id        BIGINT REFERENCES course (course_id),
    file_type        VARCHAR(10)  NOT NULL,
    category         VARCHAR(20)  NOT NULL CHECK (category IN ('lecture', 'assignment', 'project', 'attachment')),
    url              VARCHAR(255) NOT NULL,
    downloadable     BOOLEAN      NOT NULL,
    chapter          INT          NOT NULL,
    courseware_order INT          NOT NULL,
    variant_of       BIGINT,
    version          INT          NOT NULL,
    display_version  BOOLEAN      NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_student
(
    id         BIGSERIAL PRIMARY KEY,
    course_id  BIGINT REFERENCES course (course_id),
    student_id BIGINT REFERENCES web_app_user (user_id),
    status     VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'enrolled', 'rejected')),
    liked      BOOLEAN     NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (course_id, student_id)
);

CREATE TABLE courseware_student
(
    id            BIGSERIAL PRIMARY KEY,
    courseware_id BIGINT  NOT NULL REFERENCES courseware (courseware_id),
    student_id    BIGINT  NOT NULL REFERENCES web_app_user (user_id),
    completed     BOOLEAN NOT NULL,
    UNIQUE (courseware_id, student_id)
);

CREATE TABLE comment
(
    comment_id      BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL REFERENCES web_app_user (user_id),
    course_id       BIGINT       NOT NULL REFERENCES course (course_id),
    message         VARCHAR(300) NOT NULL,
    attachment      VARCHAR(255),
    attachment_type VARCHAR(10),
    reply_id        BIGINT REFERENCES comment,
    created_at      TIMESTAMP
);

CREATE TABLE rating
(
    rating_id           BIGSERIAL PRIMARY KEY,
    student_id          BIGINT REFERENCES web_app_user (user_id) NOT NULL,
    course_id           BIGINT REFERENCES course (course_id)     NOT NULL,
    overall_rating      FLOAT                                    NOT NULL,
    content_quality     FLOAT                                    NOT NULL,
    teaching_competence FLOAT                                    NOT NULL,
    workload_balance    FLOAT                                    NOT NULL,
    feedback            VARCHAR(300),
    created_at          TIMESTAMP,
    UNIQUE (student_id, course_id)
);

CREATE TABLE notification
(
    notification_id BIGSERIAL PRIMARY KEY,
    sender_email    VARCHAR(255) REFERENCES web_app_user (email) NOT NULL,
    receiver_email  VARCHAR(255) REFERENCES web_app_user (email) NOT NULL,
    subject         VARCHAR(100)                                 NOT NULL,
    text            VARCHAR(500)                                 NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stream
(
    id          SERIAL PRIMARY KEY,
    teacher_id  BIGINT REFERENCES web_app_user (user_id) UNIQUE NOT NULL,
    stream_key  VARCHAR(100) UNIQUE,
    url         VARCHAR(300) UNIQUE,
    title       VARCHAR(100),
    description VARCHAR(300)
);


INSERT INTO role (role_name)
VALUES ('ADMIN');
INSERT INTO role (role_name)
VALUES ('STUDENT');
INSERT INTO role (role_name)
VALUES ('INSTRUCTOR');
INSERT INTO role (role_name)
VALUES ('PUBLIC');

INSERT INTO web_app_user (full_name, email, password, role_id, enabled, verification_token, gender, bio, points,
                          created_at, profile_picture)
VALUES ('Public User', 'sussycourses@gmail.com', '$2a$10$O2X7nLyPmmGr5EmTRAK5G./e3x1pkneX1/v4emOeBnSOj2Buv9iK.', 4,
        true, 'ce4103d6-de74-4d64-ac0a-4dee043ff67c', 'Others', NULL, 0, '2024-11-17 09:16:28.935239', NULL),
       ('Admin', 'henglayhok@gmail.com', '$2a$10$bwNu4hxiylq6RnEljauPBuC5rFrbXIQpxKdXDa/SFO/aYctWRGK5K', 1, true,
        '87c051f7-5613-49ab-968c-99921a77c612', 'Male', NULL, 0, '2024-11-17 09:16:28.935239', NULL),
       ('L Lawliet', 'hoklayheng33@gmail.com', '$2a$10$znvpPc2Doq4EmyOC8PgeAOCjqpA/X.lHbqunjXvHVywEWJOpa7Y8.', 2, true,
        '9cfc5093-02cd-4e64-8b72-560405aa72d1', 'Male',
        'A brilliant detective with eccentric habits and unmatched deductive reasoning.', 0,
        '2024-11-17 09:16:28.935239', 'l'),
       ('Kakashi Hatake', 'fbringer99@gmail.com', '$2a$10$HHzqkBErJo3jrrc9Zy/zRuiM0xdkz33CY4b4NR40bgvrZrhYANIiK', 3,
        true, 'a3c8731a-1c73-4753-ae5e-ba2e1e1698b1', 'Male',
        'An elite ninja known for his Sharingan and relaxed but skilled teaching style.', 0,
        '2024-11-17 09:16:28.935239', 'kakashi'),
       ('Gin Ichimaru', 'aarontan113388@gmail.com', '$2a$10$hIe4TvNph/KXR0uwh/DGJ.LAtcV/mYe5eQv6GhLQP.gNad5AhZ/sm', 2,
        true, '00b32a7b-3775-4371-99c4-195a85697784', 'Male',
        'A sly and enigmatic figure with a perpetually smiling face and hidden motives.', 0,
        '2024-11-17 20:14:27.229069', 'gin'),
       ('Grimmjow Jaegerjaquez', 'sto@paragonisc.edu.kh',
        '$2a$10$2kr8dhrAGhOaXdnV60fvM.EbnxZ2GCC.K1BDzfGPEdSB5rIFrp6ae', 2, true, '981bcafe-42fd-4978-9d0d-fe570dcf4f6f',
        'Male', 'A fierce and impulsive warrior with a passion for battle and an unyielding will.', 0,
        '2024-11-17 20:18:02.915353', 'grimmjow'),
       ('Askin Nakk Le Vaar', 'tosaony33@gmail.com', '$2a$10$vZW095fH5KT7EBmq/40FhOqjFCt8GShhlPKFroETGGZFXdNvYJ0OK',
        3, true, 'f9a9fc2b-1781-49c0-b1ef-56c27195d775', 'Others',
        'A laid-back yet deadly combatant known for his analytical mind and quirky personality.', 0,
        '2024-11-17 20:22:36.626827', 'askin'),
       ('Kisuke Urahara', '12210736@mail.sustech.edu.cn',
        '$2a$10$oV2TCELdlKRPQ8lW8Jovk.K87QMFdlHGefVCZsTp5nmmZ.JoRWxjW', 3, true, '4ef2873e-f1c5-4588-9466-eaabb3bb0a18',
        'Male', 'A genius inventor and strategist with a carefree demeanor and a mysterious past.', 0,
        '2024-11-17 20:24:48.799666', 'kisuke'),
       ('Pernida Parnkgjas', 'prakbunlong53@gmail.com', '$2a$10$ZepAvGWvJrriCIK54MIRQOPQuu3T/S1PqBBCcqf83tYe0A2bjQyyO',
        2, true, '0f3c3044-7b0c-483f-bccb-b84a860e28a4', 'Others',
        'A mysterious and adaptable individual with a complex nature.', 0, now(), 'pernida'),
       ('Master Roshi', 'bprak@paragoniu.edu.kh', '$2a$10$oI4bxsig9qZ7Hv8ADe7pB.8aO3qfrlAsHmtHvVUd5ZS6i2H6e1xUC', 3,
        true, '3f60eda4-f140-4b80-abd7-a8dbc1db9685', 'Male',
        'An eccentric and wise martial arts instructor with decades of experience.', 0, now(), 'roshi'),
       ('Koro Sensei', 'esok@paragoniu.edu.kh', '$2a$10$gqs6Lv0t0IbKEU2RhRxgXObxDoXRQeXkXP6DLj7cyPZtN1Csygmu2', 3, true,
        '53ca85fe-f944-4594-9d00-aa64a6bef7f2', 'Male',
        'A highly skilled and unorthodox teacher with a deep commitment to his students.', 0, now(), 'koro'),
       ('Satoru Gojo', 'pvann2@paragoniu.edu.kh', '$2a$10$pi3l9aNalE/mz.smaq6NTeC1qXxzubyILSL1BvYRdlg2BSZLODlKW', 3,
        true, '4c1d1a98-4bb6-4978-95ae-e8206435f09f', 'Male',
        'A charismatic and immensely powerful instructor known for his unrivaled skill.', 0, now(), 'satoru'),
       ('Tsunade Senju', 'shor1@paragoniu.edu.kh', '$2a$10$1zqbvjYJSFpuOYN.u9Ai3.MRCVbfC6sk5XE8jc9CJU9mWmzGEQzT2', 3,
        true, '26d3d22a-8802-4577-a605-59fbdbfec664', 'Female',
        'A brilliant and compassionate instructor with a focus on healing and strategy.', 0, now(), 'tsunade'),
       ('Shota Aizawa', 'bzhou@paragoniu.edu.kh', '$2a$10$3YaoPOjJqjh0xyTcMeQoseKpcVWS94QSfInv1YTJ.v1JDssHYQWrC', 3,
        true, 'b3cce129-307a-4331-aede-a186763eecd1', 'Male',
        'A reserved and practical instructor who demands discipline and excellence.', 0, now(), 'shota'),
       ('Light Yagami', '12212027@mail.sustech.edu.cn', '$2a$10$TRVlt5JmOKqyElJCtG3QBuJuk.VNuHN9ER/ImQIxvf0eDyh9tRBJa',
        2, true, '7bf8cb47-71c8-41ff-85f8-8a78c2a1d5d0', 'Male',
        'A highly intelligent and ambitious student with a sharp mind.', 0, now(), 'light'),
       ('Lille Barro', '12211456@mail.sustech.edu.cn', '$2a$10$omCr98U5poZgkbsMPRcQBOp4sv7/mmsAYZCYvpwucRCAnlXiNt6DC',
        2, true, '720e0a59-cbe9-4c36-994c-638fe943aa0d', 'Male',
        'A determined and calculating student with an eye for detail.', 0, now(), 'lille'),
       ('Nagato Uzumaki', '12212025@mail.sustech.edu.cn',
        '$2a$10$MklPSxMNJOG8NoDEgNPH3e4sbUrUBJz1zBWLOpS1IxQW.d7akd7U2', 2, true, '39e782a5-79c3-456c-b0a3-340c21aa2421',
        'Male', 'A passionate student deeply driven by his ideals.', 0, now(), 'nagato'),
       ('Gerard Valkyrie', '12212643@mail.sustech.edu.cn',
        '$2a$10$7rSiDP6brHsMLX7q6rbgu.g6kYNWQhepnV.xPrBVMonECVS5Oz11K', 2, true, 'fe892bbb-1de3-4d2c-a561-fa3c9dc75f40',
        'Male', 'A bold and self-assured student with a flair for leadership.', 0, now(), 'gerard'),
       ('Nnoitra Gilga', '12113053@mail.sustech.edu.cn', '$2a$10$E.qrd1w.YwHuaE1mHoeIFeuXXcUxBlyh/nPsyK9uqJoVmxoHbX6hS',
        2, true, 'ec083433-0bdf-4eb8-a153-c1f7fefd174d', 'Male',
        'An ambitious student known for his relentless determination.', 0, now(), 'nnoritra'),
       ('Kenpachi Zaraki', '12212231@mail.sustech.edu.cn',
        '$2a$10$UG9zLBuTXfMdpeYJWWEgy.UfgC.qV1UEq9AGW7.IXau5.02IQEvRC', 2, true, '553047d0-34a0-4bb0-883e-646ee45af7a8',
        'Male', 'An energetic and daring student who loves challenges.', 0, now(), 'kenpachi'),
       ('Coyote Stark', '12210729@mail.sustech.edu.cn', '$2a$10$68eDSutzbGDFzKBH9AwmB.ZNRNy2mFNMV/VPgbNnSknnRlYbXadbW',
        2, true, '4b94cd67-625f-4bcc-a1b8-17baaeb840f2', 'Male',
        'A calm and introspective student with a sharp intellect.', 0, now(), 'coyote'),
       ('Ulquiorra Cifer', '12211905@mail.sustech.edu.cn',
        '$2a$10$IBEy.QhTsv634ZeJPKvQ9eLHZeDTnjiI3dvVrXFx1zqMxWY3Xs0qW', 2, true, '5c62ddac-fdbc-4521-b34f-90db63ff1984',
        'Male', 'A quiet and analytical student with a mysterious demeanor.', 0, now(), 'ulquiorra'),
       ('Kousei Arima', '12210530@mail.sustech.edu.cn', '$2a$10$HqpanQPLsEsKlLzX5Ih5b.ly.BxnMTZ4BLXHAtpxFyYCpwTMYc/O6',
        2, true, '3b6edfbf-2e84-41f1-9ddd-f6185264ac3e', 'Male',
        'A talented and introspective student with a passion for music.', 0, now(), 'kousei'),
       ('Kaori Miyazono', '12212613@mail.sustech.edu.cn',
        '$2a$10$7J1V8sUIP8imTfGBVxHV3.G3TMMEdhm/LsJiUCdVlJgEFNopCTWJG', 2, true, 'df00e55c-33a7-43ce-8280-cf3c33b5d403',
        'Female', 'A vibrant and passionate student who inspires others through music.', 0, now(), 'kaori'),
       ('Oetsu Nimaiya', '12212618@mail.sustech.edu.cn', '$2a$10$cJkWcfxOHJAri2yVAJjkCu2B9WnlxMcunwtbAuOrbZm6kKIUEoxdG',
        2, true, '8fa2a8ea-6b8e-42aa-b4fd-14c1453eae3', 'Male',
        'A creative and inventive student known for his unique perspectives.', 0, now(), 'oetsu'),
       ('Monkey D. Luffy', '12212309@mail.sustech.edu.cn',
        '$2a$10$IsxCBi7ykvpWQKzNuh8fSe7HCNEyYDj/nWYHKj.oZaz3Lg5Z3HLoy', 2, true, '5d0515dc-d3da-40df-97a1-9b7f91e42156',
        'Male', 'An energetic and optimistic student with big dreams.', 0, now(), 'luffy'),
       ('Roronoa Zoro', '12212501@mail.sustech.edu.cn', '$2a$10$rMxDQ2GwdvOFf4/1AZwxtOZjaqBt5RcqGpS/dxTrEo/Vi52QwG1Gi',
        2, true, '6d45d23c-25a8-46f1-b3e4-56b9bc1038aa', 'Male',
        'A determined and disciplined swordsman with unwavering focus.', 0, now(), 'zoro'),
       ('Sanji Vinsmoke', '12212402@mail.sustech.edu.cn',
        '$2a$10$2T7/S9hAKxWuzP3lmM79Ae0tVxyLHOA9UZtThKfu3/ZxKLMCq4Anq', 2, true, '9d56fe22-92ba-403b-9f90-07dffafae7c1',
        'Male', 'A skilled and charismatic cook with a fiery personality.', 0, now(), 'sanji'),
       ('Nami', '12212303@mail.sustech.edu.cn', '$2a$10$bBPI4T5pO8nF79cLfdmHDeZzRu9IrMH5UWrkK/gO0csIaIVuH7bHa', 2, true,
        '8d376c3b-2926-4f79-a7bc-49072c401ae6', 'Female',
        'A clever and resourceful navigator with a talent for strategy.', 0, now(), 'nami'),
       ('Usopp', '12210726@mail.sustech.edu.cn', '$2a$10$OujQGApuPH6AEivM8OwsM.K..ke0U8zkwT6Y3koDLTdu/bBS3QMq.', 2,
        true, 'bfdeef6c-7f00-441a-864c-573596891974', 'Others',
        'A creative and resourceful student with a knack for storytelling.', 0, now(), 'usopp');


INSERT INTO course (course_name, description, teacher_id, type, status, topic, total_evaluation_score, num_evaluations,
                    like_count, created_at, cover_image)
VALUES ('C/C++ System Design', 'You shall learn memory management as a programmer.', 4, 'open', 'approved',
        'Programming', 5.0, 1, 3, NOW(), 'c_cpp.jpeg'),
       ('IELTS Listening', 'Improve your English listening skills for the IELTS exam.', 8, 'open', 'approved',
        'Languages', 7.0, 2, 1, NOW(), 'listening.webp'),
       ('Principles of Database', 'Learn the fundamentals of database systems and their applications.', 4, 'semi-open',
        'pending', 'Software', 0, 0, 0, NOW(), 'database.jpg'),
       ('Chinese History', 'Explore the rich and fascinating history of China.', 8, 'open', 'approved', 'Chinese', 3.5,
        1, 2, NOW(), 'chinese_history.jpg'),
       ('Data Structure and Algorithm Analysis', 'Understand and analyze core data structures and algorithms.', 7,
        'non-open', 'approved', 'Programming', 0, 0, 0,
        NOW(), 'dsaa.jpg'),
       ('Chinese Culture', 'Dive into the traditions and cultural practices of China.', 4, 'semi-open', 'approved',
        'Chinese', 9.5, 2, 1, NOW(),
        'culture.webp'),
       ('Introduction to Java Programming', 'A comprehensive guide to learning Java programming.', 4, 'open',
        'approved', 'Programming', 4.5, 1, 1, NOW(),
        'java.jpg'),
       ('Calculus', 'Master the fundamental concepts of calculus, including derivatives and integrals.', 7, 'open',
        'approved', 'Math', 8.5, 2, 1, NOW(), 'calculus.jpg'),
       ('Digital Logic', 'Learn the basics of digital systems and logic design.', 4, 'open', 'approved', 'Hardware',
        7.0, 2, 1, NOW(), 'dl.jpg'),
       ('Computer Organization', 'Study the structure and behavior of computer systems.', 4, 'open', 'rejected',
        'Hardware', 0, 0, 0, NOW(), 'comp_org.jpg'),
       ('Discrete Math', 'Discover mathematical structures used in computer science.', 7, 'open', 'approved', 'Math',
        7.0, 2, 2, NOW(), 'discrete.jpg'),
       ('Linear Algebra', 'Understand the concepts of vectors, matrices, and linear transformations.', 8, 'open',
        'pending', 'Math', 0, 0, 0, NOW(), 'linear.jpg'),
       ('Principles of Physics', 'Learn the foundational principles of physics and their applications.', 4, 'open',
        'approved', 'Science', 0, 0, 0, NOW(), 'physics.webp'),
       ('Introduction to Chemistry', 'A beginner-friendly course on chemistry basics.', 4, 'semi-open', 'approved',
        'Science', 0, 0, 0,
        NOW(), 'chem.jpg'),
       ('Basic Life Science', 'Explore the fundamental principles of life sciences.', 4, 'open', 'approved', 'Science',
        0, 0, 0, NOW(), 'life.webp'),
       ('Business Mindset', 'Develop a mindset for business success and growth.', 7, 'semi-open', 'approved',
        'Business', 0, 0, 0,
        NOW(), 'business.webp'),
       ('Marketing as a Lifetime Skill', 'Learn the essentials of marketing that last a lifetime.', 4, 'open',
        'approved', 'Business', 0, 0, 0,
        NOW(), 'marketing.png'),
       ('Japanese', 'Start your journey into learning the Japanese language.', 4, 'semi-open', 'approved', 'Languages',
        0, 0, 0,
        NOW(), 'nihongo.jpg'),
       ('Machine Learning and Big Data Management', 'Dive into the world of machine learning and managing big data.', 4,
        'semi-open', 'approved', 'Data Science', 0, 0, 0,
        NOW(), 'ml.jpg'),
       ('World Music', 'Explore music traditions from around the globe.', 4, 'non-open', 'approved', 'Art', 0, 0, 0,
        NOW(), 'music.jpeg'),
       ('Elementary Chinese 1', '早上好中国。', 4, 'non-open', 'approved', 'Languages', 0, 0, 0,
        NOW(), 'chinese1.jpg');;

INSERT INTO courseware (course_id, file_type, category, url, downloadable, chapter, courseware_order,
                        variant_of, version, display_version, created_at)
VALUES (1, 'pptx', 'lecture', 'Lecture01.pptx', false, 1, 1, 1, 1, true, '2024-12-04 11:18:54.071622'),
       (1, 'md', 'project', 'project1.md', true, 1, 1, 2, 1, true, '2024-12-04 11:19:32.584331'),
       (1, 'md', 'project', 'project2.md', true, 1, 1, 3, 1, true, '2024-12-04 11:20:00.963486'),
       (2, 'pdf', 'lecture', 'Lecture 1.pdf', true, 1, 1, 5, 1, true, '2024-12-04 11:29:31.896166'),
       (2, 'pdf', 'lecture', 'Answer Key (Listening Lecture 1).pdf', true, 1, 1, 6, 1, true,
        '2024-12-04 11:29:56.307501'),
       (4, 'pdf', 'lecture', 'week 2.pdf', true, 2, 1, 6, 1, true, '2024-12-04 11:45:50.397345'),
       (4, 'pdf', 'lecture', 'week 4.pdf', true, 4, 1, 7, 1, true, '2024-12-04 11:46:03.850336'),
       (4, 'mp4', 'attachment', 'yt1s.com - Peking Man Site at Zhoukoudian UNESCONHK_360p.mp4', true, 1, 1, 8, 1,
        true, '2024-12-04 11:47:27.847548'),
       (7, 'pdf', 'lecture', 'Lab 1 - Set up JDK.pdf', true, 1, 1, 9, 1, true, '2024-12-04 11:59:11.492698'),
       (7, 'pdf', 'lecture', 'Part02-IDEA.pdf', true, 2, 1, 10, 1, true, '2024-12-04 11:59:37.332235'),
       (7, 'java', 'attachment', 'Demo.java', true, 3, 1, 11, 1, true, '2024-12-04 11:59:37.332235'),
       (8, 'pdf', 'lecture', '2022calculus1.pdf', true, 1, 1, 12, 1, true, '2024-12-04 13:41:14.735840'),
       (5, 'pdf', 'lecture', 'dsaa_L01_intro.pdf', true, 1, 1, 13, 1, true, '2024-12-04 14:08:41.468804'),
       (5, 'pdf', 'lecture', 'dsaa_L02_algorithm.pdf', true, 2, 1, 14, 1, true, '2024-12-04 14:09:01.080831'),
       (5, 'pdf', 'lecture', 'dsaa_L03_sorting.pdf', true, 3, 1, 15, 1, true, '2024-12-04 14:09:09.596356'),
       (9, 'pdf', 'lecture', 'DigitalLogic_23F_Lec2_Boolean_Algebra.pdf', true, 1, 1, 16, 1, true,
        '2024-12-04 14:15:18.438480'),
       (11, 'pdf', 'assignment', 'Assign01(1).pdf', true, 1, 1, 17, 1, true, '2024-12-04 14:17:22.127951'),
       (11, 'pdf', 'assignment', 'Assign02(1).pdf', true, 2, 1, 18, 1, true, '2024-12-04 14:19:37.066159'),
       (11, 'pdf', 'assignment', 'Assign03(1).pdf', true, 3, 1, 19, 1, true, '2024-12-04 14:19:45.719268'),
       (6, 'pdf', 'lecture', 'Chapter 1 Chinese Philosophy.pdf', true, 1, 1, 20, 1, true, '2024-12-04 14:23:41.374710'),
       (6, 'pdf', 'lecture', 'Chapter 1 Chinese Philosophy and Religion II.pdf', true, 1, 1, 21, 1, true,
        '2024-12-04 14:23:49.270532'),
       (6, 'pdf', 'lecture', 'Chapter 2 Chinese Literature I -Classical Literature - simplifiedversion.pdf', true, 2, 1,
        22, 1, true, '2024-12-04 14:23:56.155228'),
       (6, 'pdf', 'lecture', 'Attachment Tea wine Chinese Costume.pdf', false, 1, 1, 23, 1, true,
        '2024-12-04 14:24:28.901710'),
       (13, 'pdf', 'lecture', 'ch1.pdf', true, 1, 1, 24, 1, true, '2024-12-04 14:43:02.853941'),
       (13, 'pdf', 'lecture', 'ch2.pdf', true, 2, 1, 25, 1, true, '2024-12-04 14:43:12.456519'),
       (13, 'pdf', 'lecture', 'ch3.pdf', true, 3, 1, 26, 1, true, '2024-12-04 14:43:20.210078');

INSERT INTO course_student (course_id, student_id, status, liked)
VALUES (1, 3, 'enrolled', TRUE),
       (2, 3, 'enrolled', FALSE),
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
       (11, 3, 'enrolled', TRUE),
       (1, 5, 'enrolled', TRUE),
       (1, 6, 'enrolled', TRUE),
       (13, 3, 'enrolled', FALSE),
       (13, 5, 'enrolled', FALSE);

INSERT INTO courseware_student (courseware_id, student_id, completed)
VALUES (1, 3, TRUE),
       (2, 3, TRUE),
       (3, 3, FALSE),
       (4, 3, TRUE),
       (5, 3, FALSE),
       (6, 3, TRUE),
       (7, 3, FALSE),
       (8, 3, FALSE),
       (4, 6, FALSE),
       (5, 6, TRUE),
       (6, 5, FALSE),
       (7, 5, FALSE),
       (8, 5, FALSE),
       (20, 3, FALSE),
       (21, 3, TRUE),
       (22, 3, TRUE),
       (23, 3, FALSE),
       (12, 6, TRUE),
       (16, 5, FALSE),
       (9, 6, FALSE),
       (10, 6, FALSE),
       (11, 6, FALSE),
       (17, 5, FALSE),
       (18, 5, TRUE),
       (19, 5, TRUE),
       (12, 3, FALSE),
       (16, 6, FALSE),
       (4, 5, TRUE),
       (5, 5, FALSE),
       (20, 5, FALSE),
       (21, 5, FALSE),
       (22, 5, FALSE),
       (23, 5, FALSE),
       (17, 3, FALSE),
       (18, 3, FALSE),
       (19, 3, FALSE),
       (1, 5, TRUE),
       (2, 5, FALSE),
       (3, 5, FALSE),
       (1, 6, FALSE),
       (2, 6, FALSE),
       (3, 6, TRUE),
       (24, 3, FALSE),
       (25, 3, FALSE),
       (26, 3, TRUE),
       (24, 5, FALSE),
       (25, 5, FALSE),
       (26, 5, TRUE);


INSERT INTO comment (user_id, course_id, message, attachment, attachment_type, reply_id, created_at)
VALUES (3, 1, 'Awesome site, awesome course, what else can I say?', NULL, NULL, NULL, '2023-10-01T12:00:00Z'),
       (5, 1, 'Wholeheartedly agree!', NULL, NULL, 1, '2023-10-02T09:30:00Z'),
       (6, 1, 'I learned so much from this course. Check my notes!', 'note', 'pdf', NULL,
        '2023-10-03T14:15:00Z');


INSERT INTO rating (course_id, student_id, overall_rating, content_quality, teaching_competence,
                    workload_balance, feedback, created_at)
VALUES (2, 6, 4.0, 4.5, 4.0, 3.5, 'Good content, clear explanations.', NOW()),
       (4, 5, 3.5, 3.5, 4.0, 3.0, 'Fairly balanced, needs more examples.', NOW()),
       (6, 3, 5.0, 5.0, 5.0, 5.0, 'Exceptional course, highly recommend.', NOW()),
       (8, 6, 4.5, 4.5, 4.5, 4.5, 'Engaging and insightful.', NOW()),
       (9, 6, 3.0, 2.5, 3.5, 3.0, 'A bit basic, but helpful.', NOW()),
       (9, 5, 4.0, 4.0, 4.0, 4.0, 'Solid course with practical examples.', NOW()),
       (7, 6, 4.5, 4.5, 5.0, 4.0, 'Instructor was very competent.', NOW()),
       (11, 5, 3.5, 3.5, 3.0, 4.0, 'Content is good, but workload is high.', NOW()),
       (8, 3, 4.0, 4.5, 3.5, 4.0, 'Good structure, some room for improvement.', NOW()),
       (2, 5, 3.0, 3.0, 3.5, 2.5, 'Average experience overall.', NOW()),
       (6, 5, 4.5, 4.5, 4.5, 4.5, 'Interesting insights, well-structured.', NOW()),
       (3, 11, 3.5, 3.0, 4.0, 3.5, 'Good content but needs better pacing.', NOW()),
       (1, 3, 5.0, 5.0, 5.0, 5.0, 'Literally the best Java course out there!', NOW());


INSERT INTO notification (sender_email, receiver_email, subject, text, created_at)
VALUES ('fbringer99@gmail.com', 'hoklayheng33@gmail.com', 'Welcome to my course!',
        'Thank you for joining my course! I hope you enjoy your learning experience.', '2023-10-01T12:00:00Z'),
       ('fbringer99@gmail.com', 'hoklayheng33@gmail.com', 'Important', 'You have a project deadline in 3 days.',
        '2023-10-01T12:00:00Z'),
       ('hoklayheng33@gmail.com', 'fbringer99@gmail.com', 'Test', 'Test', '2023-10-02T09:30:00Z');

INSERT INTO stream(teacher_id, stream_key, url, title, description)
VALUES (4, '0ff6bfb7-1226-47ea-901a-1bbdcfe16671', 'https://embed.api.video/live/li4IiMlSifoak2VDjs6pDNTm',
        'I will teach you stuff', 'This stream is me showcasing nothing'),
       (7, NULL, NULL, 'I will teach you stuff', 'This stream is me showcasing nothing'),
       (8, NULL, NULL, 'I will teach you stuff', 'This stream is me showcasing nothing');

END;