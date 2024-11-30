BEGIN;

DROP TABLE IF EXISTS courseware_student CASCADE;
DROP TABLE IF EXISTS course_student CASCADE;
DROP TABLE IF EXISTS course_courseware CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS courseware CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
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
    file_type        VARCHAR(10)  NOT NULL CHECK (file_type IN ('md', 'pdf', 'mp4', 'pptx')),
    category         VARCHAR(20)  NOT NULL CHECK (category IN ('lecture', 'assignment', 'project')),
    url              VARCHAR(255) NOT NULL UNIQUE,
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
    courseware_id BIGINT REFERENCES courseware (courseware_id),
    student_id    BIGINT REFERENCES web_app_user (user_id),
    completed     BOOLEAN NOT NULL
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
        true, 'ce4103d6-de74-4d64-ac0a-4dee043ff67c', 'Other', NULL, 0, '2024-11-17 09:16:28.935239', null),
       ('Xing', 'henglayhok@gmail.com', '$2a$10$bwNu4hxiylq6RnEljauPBuC5rFrbXIQpxKdXDa/SFO/aYctWRGK5K', 1, true,
        '87c051f7-5613-49ab-968c-99921a77c612', 'Male', NULL, 0, '2024-11-17 09:16:28.935239', null),
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
       ('Grimmjow Jaegerjaquez', '12212027@mail.sustech.edu.cn',
        '$2a$10$2kr8dhrAGhOaXdnV60fvM.EbnxZ2GCC.K1BDzfGPEdSB5rIFrp6ae', 2, true, '981bcafe-42fd-4978-9d0d-fe570dcf4f6f',
        'Male', 'A fierce and impulsive warrior with a passion for battle and an unyielding will.', 0,
        '2024-11-17 20:18:02.915353', 'grimmjow'),
       ('Askin Nakk Le Vaar', 'lhok1@paragoniu.edu.kh', '$2a$10$vZW095fH5KT7EBmq/40FhOqjFCt8GShhlPKFroETGGZFXdNvYJ0OK',
        3, true, 'f9a9fc2b-1781-49c0-b1ef-56c27195d775', 'Other',
        'A laid-back yet deadly combatant known for his analytical mind and quirky personality.', 0,
        '2024-11-17 20:22:36.626827', 'askin'),
       ('Kisuke Urahara', '12210736@mail.sustech.edu.cn',
        '$2a$10$oV2TCELdlKRPQ8lW8Jovk.K87QMFdlHGefVCZsTp5nmmZ.JoRWxjW', 3, true, '4ef2873e-f1c5-4588-9466-eaabb3bb0a18',
        'Male', 'A genius inventor and strategist with a carefree demeanor and a mysterious past.', 0,
        '2024-11-17 20:24:48.799666', 'kisuke');


INSERT INTO course (course_name, description, teacher_id, type, status, topic, total_evaluation_score, num_evaluations,
                    like_count, created_at)
VALUES ('Java - Beginner to Advanced', 'blank', 4, 'open', 'approved', 'Programming', 5.0, 1, 3, NOW()),
       ('Data Management', 'blank.', 8, 'open', 'approved', 'Data Science', 7.0, 2, 1, NOW()),
       ('Spring Boot', 'blank', 4, 'semi-open', 'pending', 'Web Development', 0, 0, 0, NOW()),
       ('Learn Axios', 'blank', 8, 'open', 'approved', 'Web Development', 3.5, 1, 2, NOW()),
       ('Connecting frontend to backend', 'blank', 7, 'non-open', 'approved', 'Web Development', 0, 0, 0, NOW()),
       ('Intro to web development', 'blank', 4, 'open', 'approved', 'Web Development', 9.5, 2, 1, NOW()),
       ('How to sell a book', 'blank', 4, 'open', 'approved', 'Marketing', 4.5, 1, 1, NOW()),
       ('Interior Design', 'blank', 7, 'open', 'approved', 'Design', 8.5, 2, 1, NOW()),
       ('FPGA changes your life', 'blank', 4, 'open', 'approved', 'Hardware', 7.0, 2, 1, NOW()),
       ('Tips to become a billionaire', 'blank', 4, 'open', 'rejected', 'Finance', 0, 0, 0, NOW()),
       ('Learn about Inflation', 'blank', 7, 'open', 'approved', 'Economics', 7.0, 2, 2, NOW()),
       ('Lead your followers', 'blank', 8, 'open', 'pending', 'Leadership', 0, 0, 0, NOW()),
       ('A million dollar business is not a dream', 'blank', 4, 'semi-open', 'approved', 'Entrepreneurship', 0, 0,
        0, NOW());


INSERT INTO courseware (course_id, file_type, category, url, downloadable, chapter, courseware_order, variant_of,
                        version, display_version, created_at)
VALUES (13, 'mp4', 'lecture', 'chapter1', FALSE, 1, 1, 1, 1, TRUE, NOW()),
       (13, 'pdf', 'lecture', 'intro_python.pdf', FALSE, 1, 2, 2, 1, TRUE, NOW()),
       (1, 'mp4', 'lecture', 'advanced_js.mp4', FALSE, 1, 3, 3, 1, TRUE, NOW()),
       (2, 'md', 'assignment', 'datascience_overview.md', FALSE, 1, 1, 4, 1, TRUE, NOW()),
       (1, 'mp4', 'lecture', 'chapter1_version2', FALSE, 1, 1, 1, 2, FALSE, NOW()),
       (1, 'pdf', 'assignment', 'Unsupervised learning.pdf', FALSE, 1, 1, 1, 1, TRUE, NOW()),
       (1, 'mp4', 'lecture', 'lecture2.mp4', FALSE, 2, 1, 1, 1, TRUE, NOW()),
       (1, 'pptx', 'project', 'Overview.pptx', FALSE, 1, 1, 1, 1, TRUE, NOW()),
       (13, 'md', 'project', 'README.md', FALSE, 2, 1, 1, 1, TRUE, NOW())
;;

INSERT INTO course_student (course_id, student_id, status, liked)
VALUES (1, 3, 'enrolled', TRUE),
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
       (11, 3, 'enrolled', TRUE),
       (1, 5, 'enrolled', TRUE),
       (1, 6, 'enrolled', TRUE),
       (13, 3, 'pending', FALSE),
       (13, 5, 'pending', FALSE);

INSERT INTO comment (user_id, course_id, message, attachment, attachment_type, reply_id, created_at)
VALUES (3, 1, 'Awesome site, awesome course, what else can I say?', NULL, NULL, NULL, '2023-10-01T12:00:00Z'),
       (5, 1, 'Wholeheartedly agree!', NULL, NULL, 1, '2023-10-02T09:30:00Z'),
       (6, 1, 'I learned so much from this course. Check my notes!', 'note', 'pdf', NULL,
        '2023-10-03T14:15:00Z');


INSERT INTO rating (rating_id, course_id, student_id, overall_rating, content_quality, teaching_competence,
                    workload_balance, feedback, created_at)
VALUES (1, 2, 6, 4.0, 4.5, 4.0, 3.5, 'Good content, clear explanations.', NOW()),
       (2, 4, 5, 3.5, 3.5, 4.0, 3.0, 'Fairly balanced, needs more examples.', NOW()),
       (3, 6, 3, 5.0, 5.0, 5.0, 5.0, 'Exceptional course, highly recommend.', NOW()),
       (4, 8, 6, 4.5, 4.5, 4.5, 4.5, 'Engaging and insightful.', NOW()),
       (5, 9, 6, 3.0, 2.5, 3.5, 3.0, 'A bit basic, but helpful.', NOW()),
       (6, 9, 5, 4.0, 4.0, 4.0, 4.0, 'Solid course with practical examples.', NOW()),
       (7, 7, 6, 4.5, 4.5, 5.0, 4.0, 'Instructor was very competent.', NOW()),
       (8, 11, 5, 3.5, 3.5, 3.0, 4.0, 'Content is good, but workload is high.', NOW()),
       (9, 8, 3, 4.0, 4.5, 3.5, 4.0, 'Good structure, some room for improvement.', NOW()),
       (10, 2, 5, 3.0, 3.0, 3.5, 2.5, 'Average experience overall.', NOW()),
       (11, 6, 5, 4.5, 4.5, 4.5, 4.5, 'Interesting insights, well-structured.', NOW()),
       (12, 11, 3, 3.5, 3.0, 4.0, 3.5, 'Good content but needs better pacing.', NOW()),
       (13, 1, 3, 5.0, 5.0, 5.0, 5.0, 'Literally the best Java course out there!', NOW());


INSERT INTO notification (sender_email, receiver_email, subject, text, created_at)
VALUES ('fbringer99@gmail.com', 'hoklayheng33@gmail.com', 'Welcome to my course!',
        'Thank you for joining my course! I hope you enjoy your learning experience.', '2023-10-01T12:00:00Z'),
       ('fbringer99@gmail.com', 'hoklayheng33@gmail.com', 'Important', 'You have a project deadline in 3 days.',
        '2023-10-01T12:00:00Z'),
       ('hoklayheng33@gmail.com', 'fbringer99@gmail.com', 'Test', 'Test', '2023-10-02T09:30:00Z');

END;