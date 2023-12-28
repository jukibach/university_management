CREATE SCHEMA IF NOT EXISTS building;
CREATE SCHEMA IF NOT EXISTS curriculum;
CREATE SCHEMA IF NOT EXISTS faculty;
CREATE SCHEMA IF NOT EXISTS student;
CREATE SCHEMA IF NOT EXISTS salary;
CREATE SCHEMA IF NOT EXISTS timetable;
CREATE SCHEMA IF NOT EXISTS department;

create table if not exists building.buildings
(
    id          SERIAL PRIMARY KEY,
    code        varchar(15)  not null UNIQUE,
    name        varchar(255) not null UNIQUE,
    details     TEXT,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);



create table if not exists curriculum.categories
(
    id           SERIAL PRIMARY KEY,
    name         varchar(255) not null UNIQUE,
    subject_type varchar(255),
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);


create table if not exists curriculum.courses
(
    id          BIGSERIAL PRIMARY KEY,
    code        varchar(15)  not null UNIQUE,
    name        varchar(255) not null,
    credits     integer      not null,
    description varchar(255) not null,
    start_day   date         not null,
    start_time  date         not null,
    category_id bigint
        constraint fkkyes7515s3ypoovxrput029bh
            references curriculum.categories,
     created_at  TIMESTAMP    NOT NULL,
       updated_at  TIMESTAMP,
       created_by  VARCHAR(255) NOT NULL,
       updated_by  VARCHAR(255)
);

create table if not exists curriculum.curriculums
(
    id           SERIAL PRIMARY KEY,
    name          varchar(255) not null,
    year          integer      not null,
    total_credits smallint     not null,
      created_at  TIMESTAMP    NOT NULL,
        updated_at  TIMESTAMP,
        created_by  VARCHAR(255) NOT NULL,
        updated_by  VARCHAR(255)
);

create table if not exists curriculum.curriculum_course
(
    id            SERIAL PRIMARY KEY,
    course_id     bigint not null
        constraint fklfushql5xpxtifenls8jr288q
            references curriculum.courses,
    curriculum_id bigint not null
        constraint fk279hdvixevg1uf9u76u7kxamt
            references curriculum.curriculums
);

create table if not exists department.departments
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255) not null,
    description varchar(255) not null,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);


create table if not exists faculty.faculties
(
    id            SERIAL PRIMARY KEY,
    code          varchar(255) not null UNIQUE,
    name          varchar(255) not null,
    description   TEXT,
    curriculum_id bigint
        unique
        constraint fk5h52wi47rp1r5d09npkvhbv3e
            references curriculum.curriculums,
   created_at  TIMESTAMP    NOT NULL,
     updated_at  TIMESTAMP,
     created_by  VARCHAR(255) NOT NULL,
     updated_by  VARCHAR(255)
);

create table if not exists faculty.classes
(
    id          SERIAL PRIMARY KEY,
    code        varchar(255) not null UNIQUE,
    name        varchar(255) not null,
    faculty_id  bigint
        constraint fkpoymrbf5df7x6k2ylbxfbgaqu
            references faculty.faculties,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);


create table if not exists student.guardians
(
    id            SERIAL PRIMARY KEY,
    name          varchar(255) not null,
    phone         varchar(255) not null,
    relationships varchar(255) not null,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);

create table if not exists curriculum.prerequisites
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255),
    description varchar(255),
     created_at  TIMESTAMP    NOT NULL,
       updated_at  TIMESTAMP,
       created_by  VARCHAR(255) NOT NULL,
       updated_by  VARCHAR(255)
);
create table if not exists curriculum.course_prerequisite
(
    id              SERIAL PRIMARY KEY,
    course_id       bigint not null
        constraint fkbetn85a7saqxw89q7mia7dpcj
            references curriculum.prerequisites,
    prerequisite_id bigint not null
        constraint fk7ymu65b28wpw2opjmrx7hsbxl
            references curriculum.courses
);

create table if not exists building.rooms
(
    id            SERIAL PRIMARY KEY,
    name          varchar(255) not null UNIQUE,
    floor         varchar(255) not null,
    capacity      integer      not null,
    building_id   bigint
        constraint fk4kmfw73x2vpfymk0ml875rh2q
            references building.buildings,
    department_id bigint
        constraint fkfbxs68qt7hfp6rkqan2eqbtyq
            references department.departments,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);


create table if not exists salary.salary
(
    id          SERIAL PRIMARY KEY,
    base_salary integer      not null,
    pay_date    date         not null,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);

create table if not exists faculty.instructors
(
    id          BIGSERIAL PRIMARY KEY,
    code        varchar(20) not null UNIQUE,
    name        varchar(50) not null,
    dob         date         not null,
    email       varchar(100) not null UNIQUE,
    phone       varchar(11) not null UNIQUE,
    gender      varchar(10) not null,
    address     varchar(255) not null,
    account_id  BIGSERIAL,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account.accounts(id),
    faculty_id  bigint
        constraint fki8vktt5q456toh0o8apn8o8rf
            references faculty.faculties,
    salary_id   bigint
        constraint fkbfp539ioos6am107yk50dcugg
            references salary.salary,
      created_at  TIMESTAMP    NOT NULL,
     updated_at  TIMESTAMP,
     created_by  VARCHAR(255) NOT NULL,
     updated_by  VARCHAR(255)
);

create table if not exists faculty.course_instructor
(
    id            SERIAL PRIMARY KEY,
    course_id     bigint not null
        constraint fkeqej22fgwa29i98ucd9x9ycie
            references curriculum.courses,
    instructor_id BIGSERIAL   not null
        constraint fkltori8qni3ivrlovca4gd9yw3
            references faculty.instructors
);

create table if not exists department.managers
(
    id            BIGSERIAL PRIMARY KEY,
    code          varchar(255) not null UNIQUE,
    name          varchar(255) not null,
    dob           date         not null,
    email         varchar(255) not null UNIQUE,
    phone         varchar(255) not null UNIQUE,
    gender        varchar(255) not null,
    address       varchar(255) not null,
    account_id    BIGSERIAL
        unique
        constraint fks8vxfog0lwxdn09g7d71fkuxp
            references account.accounts,
    department_id bigint
        constraint fk78bxps40sjq2glpliqw79ewqv
            references department.departments,
    salary_id     bigint
        constraint fkm1sveer9f5qktxvx464tl7e5x
            references salary.salary,
      created_at  TIMESTAMP    NOT NULL,
        updated_at  TIMESTAMP,
        created_by  VARCHAR(255) NOT NULL,
        updated_by  VARCHAR(255)
);

create table if not exists curriculum.session
(
    id            SERIAL PRIMARY KEY,
    name          varchar(255),
    time_start    varchar(255),
    time_end      varchar(255),
    course_id     bigint       not null
        constraint fk5ibwg70x5r9hls6pqe5yg2vvb
            references curriculum.courses,
    instructor_id BIGSERIAL         not null
        constraint fk8l1v75brk9afixg1li5ymt7nj
            references faculty.instructors,
    room_id       bigint       not null
        constraint fkoyfccms1psubki0cm7c92msrp
            references building.rooms,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);

create table if not exists curriculum.year
(
    id          SERIAL PRIMARY KEY,
    school_year varchar(255) not null
);

create table if not exists curriculum.semester
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255),
    start_date  date,
    end_date    date,
    year_id     bigint       not null
        constraint fk7w0wxawgsexa0ifu262tob1ep
            references curriculum.year,
      created_at  TIMESTAMP    NOT NULL,
        updated_at  TIMESTAMP,
        created_by  VARCHAR(255) NOT NULL,
        updated_by  VARCHAR(255)
);

create table if not exists curriculum.course_semester
(
    id          SERIAL PRIMARY KEY,
    course_id   bigint
        constraint fkqo728e9f9aj1bne0m40nbkuwo
            references curriculum.courses,
    semester_id bigint
        constraint fk24ine28voavuawhlp72ogeu64
            references curriculum.semester
);

create table if not exists curriculum.exams
(
    id              SERIAL PRIMARY KEY,
    name            varchar(255) not null,
    date            date         not null,
    duration_minute varchar(255) not null,
    course_id       bigint       not null
        constraint fkiub3ue9cklcyyra24v9ns656n
            references curriculum.courses,
    instructor_id   BIGSERIAL         not null
        constraint fk23nklkf992jpo1hxqmpa16t77
            references faculty.instructors,
    semester_id     bigint       not null
        constraint fkp8njc4xlmts6sdw7p3ra2shkf
            references curriculum.semester,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);

create table if not exists timetable.timetable
(
    id          SERIAL PRIMARY KEY,
    day_of_week varchar(255),
    semester_id bigint       not null
        constraint fkmkvrn4nnpjhpjxgqvd4dhfdbl
            references curriculum.semester,
    created_at  TIMESTAMP    NOT NULL,
      updated_at  TIMESTAMP,
      created_by  VARCHAR(255) NOT NULL,
      updated_by  VARCHAR(255)
);

create table if not exists student.students
(
    id            BIGSERIAL PRIMARY KEY,
    code          varchar(255) not null UNIQUE,
    name          varchar(255) not null,
    dob           date         not null,
    email         varchar(255) not null UNIQUE,
    phone         varchar(255) not null UNIQUE,
    gender        varchar(255) not null,
    address       varchar(255) not null,
    academic_year varchar(255) not null,
    account_id    BIGSERIAL
        unique
        constraint fkoootcgotavmpat2yv9o52wx1q
            references account.accounts,
    class_id      bigint
        constraint fknsl7w2nw6o6eq53hqlxfcijpm
            references faculty.classes,
    guardian_id   bigint
        constraint fkkktpamdaydbva4kxjcn17sbvk
            references student.guardians,
    timetable_id  bigint
        constraint fk7yw3t9woq5rcao21u1a625l8t
            references timetable.timetable,
     created_at  TIMESTAMP    NOT NULL,
       updated_at  TIMESTAMP,
       created_by  VARCHAR(255) NOT NULL,
       updated_by  VARCHAR(255)
);

create table if not exists student.grade_report
(
    id               SERIAL PRIMARY KEY,
    point_process    double precision not null,
    point_end_course double precision not null,
    total_mark       double precision not null,
    grades           varchar(255),
    instructor_id    BIGSERIAL             not null
        constraint fkqkyw5ljmjiemlr82rcy5iictj
            references faculty.instructors,
    student_id       BIGSERIAL             not null
        constraint fknleqou2lxe9cqllvw26k9r709
            references student.students,
   created_at  TIMESTAMP    NOT NULL,
     updated_at  TIMESTAMP,
     created_by  VARCHAR(255) NOT NULL,
     updated_by  VARCHAR(255)
);
create table if not exists student.student_course
(
    id         SERIAL PRIMARY KEY,
    course_id  bigint not null
        constraint fkejrkh4gv8iqgmspsanaji90ws
            references curriculum.courses,
    student_id BIGSERIAL not null
        constraint fkq7yw2wg9wlt2cnj480hcdn6dq
            references student.students
);

create table if not exists student.student_exam
(
    id         SERIAL PRIMARY KEY,
    score      double precision not null,
    exam_id    bigint           not null
        constraint fkm3tx9n7w4hpjyu130hp3bueh7
            references curriculum.exams,
    student_id BIGSERIAL             not null
        constraint fko8h2f1th1vanl3169pi8vmeuw
            references student.students
);

create table if not exists timetable.timetable_session
(
    id           SERIAL PRIMARY KEY,
    session_id   bigint not null
        constraint fkfwmdmoq1uqg0mgfu8js3b35mv
            references curriculum.session,
    timetable_id bigint not null
        constraint fkjoiuomn516g8wk3me14812gyi
            references timetable.timetable
);

INSERT INTO faculty.instructors (code, name, dob, email, phone, gender, address, account_id, created_at, created_by)
VALUES ('T001', 'Nguyen Van A', '2012-12-12', 'huuvu110799@gmail.com', '0905756741', 'nam', 'Quy Nhon', 3, TIMESTAMP '2022-12-12', 'VuLh26'),
 ('T002', 'Nguyen Van C', '2012-12-12', 'huuvu1999@gmail.com', '0905756743', 'nam', 'Quy Nhon', 1, TIMESTAMP '2022-12-12', 'VuLh26'),
 ('T003', 'Nguyen Van D', '2013-12-12', 'huuvule225@gmail.com', '0905756742', 'nam', 'Binh Dinh', 2, TIMESTAMP '2022-12-12', 'VuLh26');



INSERT INTO curriculum.courses (code, name, credits, description, start_day, start_time, created_at, created_by)
VALUES
 ('COURSE001', 'Course 1', 3, 'Course 1 description', '2023-01-01', '2023-01-01 09:00:00', CURRENT_TIMESTAMP, 'Admin'),
 ('COURSE002', 'Course 2', 4, 'Course 2 description', '2023-02-01', '2023-01-01 14:00:00', CURRENT_TIMESTAMP, 'Admin'),
 ('COURSE003', 'Course 3', 2, 'Course 3 description', '2023-03-01', '2023-03-01 18:00:00', CURRENT_TIMESTAMP, 'Admin');

  INSERT INTO student.students (code, name, dob, email, phone, gender, address, academic_year, account_id, created_at, created_by)
  VALUES ('S001', 'Nguyen Van A', '2000-01-01', 'nguyenvana@example.com', '123456789', 'Male', '123 ABC Street', '2023', 19, CURRENT_TIMESTAMP, 'Admin'),
 ('S002', 'Nguyen Thi By', '2001-02-02', 'nguyenthiia1@example.com', '9876543311', 'Female', '456 XYZ Street', '2023', 18, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S003', 'Nguyen Thi B', '2001-02-02', 'nguyenthia7@example.com', '9876543211', 'Female', '456 XYZ Street', '2023', 1, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S004', 'Nguyen Thi C', '2001-02-02', 'nguyenthib2@example.com', '9876543212', 'Female', '456 XYZ Street', '2023', 2, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S005', 'Nguyen Thi D', '2001-02-02', 'nguyenthic3@example.com', '9876543213', 'Female', '456 XYZ Street', '2023', 3, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S006', 'Nguyen Thi BE', '2001-02-02', 'nguyenthid4@example.com', '9876543214', 'Female', '456 XYZ Street', '2023', 4, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S007', 'Nguyen Thi F', '2001-02-02', 'nguyenthicc5@example.com', '9876543215', 'Female', '456 XYZ Street', '2023', 20, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S008', 'Nguyen Thi G', '2001-02-02', 'nguyenthibb6@example.com', '9876543216', 'Female', '456 XYZ Street', '2023', 5, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S009', 'Nguyen Thi H', '2001-02-02', 'nguyenthiba8@example.com', '9876543217', 'Female', '456 XYZ Street', '2023', 6, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S010', 'Nguyen Thi K', '2001-02-02', 'nguyenthiby9@example.com', '9876543218', 'Female', '456 XYZ Street', '2023', 7, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S011', 'Nguyen Thi BPP','2001-02-02', 'nguyenthibi10@example.com', '9876543219', 'Female', '456 XYZ Street', '2023', 8, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S012', 'Nguyen Thi RR', '2001-02-02', 'nguyenthibo11@example.com', '9876543222', 'Female', '456 XYZ Street', '2023', 9, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S013', 'Nguyen Thi FF', '2001-02-02', 'nguyenthibp12@example.com', '9876543223', 'Female', '456 XYZ Street', '2023', 10, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S014', 'Nguyen Thi OO', '2001-02-02', 'nguyenthibq13@example.com', '9876543224', 'Female', '456 XYZ Street', '2023', 11, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S015', 'Nguyen Thi LL', '2001-02-02', 'nguyenthibi14@example.com', '9876543225', 'Female', '456 XYZ Street', '2023', 12, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S016', 'Nguyen Thi TT', '2001-02-02', 'nguyenthibm15@example.com', '9876543226', 'Female', '456 XYZ Street', '2023', 13, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S017', 'Nguyen Thi BR', '2001-02-02', 'nguyenthibf16@example.com', '9876543227', 'Female', '456 XYZ Street', '2023', 14, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S018', 'Nguyen Thi BH', '2001-02-02', 'nguyenthibe17@example.com', '9876543228', 'Female', '456 XYZ Street', '2023', 15, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S019', 'Nguyen Thi BK', '2001-02-02', 'nguyenthibr18@example.com', '9876543229', 'Female', '456 XYZ Street', '2023', 16, CURRENT_TIMESTAMP, 'VuLH26'),
  ('S020', 'Nguyen Thi BQ', '2001-02-02', 'nguyenthibe19@example.com', '9876543230', 'Female', '456 XYZ Street', '2023', 17, CURRENT_TIMESTAMP, 'VuLH26');





  INSERT INTO student.student_course (course_id, student_id)
  VALUES (1, 1),
         (2, 2),
         (1, 3),
         (3, 4),
         (1, 5),
         (3, 6),
         (2, 7),
         (1, 8),
         (2, 9),
         (3, 10),
         (2, 11),
         (2, 12),
         (1, 13),
         (3, 14),
         (1, 15),
         (1, 16),
         (2, 17),
         (3, 18),
         (3, 19),
         (3, 20);

--  INSERT INTO student.student_course (course_id, student_id)
--  VALUES (2, 2);
  INSERT INTO faculty.course_instructor (course_id, instructor_id)
  VALUES (1, 1),
  (2, 3),
  (2, 2),
  (3, 3),
  (2, 1);


  INSERT INTO student.grade_report (point_process, point_end_course, total_mark, grades, instructor_id, student_id, created_at, updated_at, created_by, updated_by)
  VALUES (8.5, 9.0, 8.8, 'A', 1, 1, CURRENT_TIMESTAMP, NULL, 'Admin', NULL),
  (5.0, 7.0, 6.6, 'b', 2, 2, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 3, 3, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 1, 4, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 2, 5, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 3, 6, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 1, 7, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 2, 8, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 3, 8, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 2, 10, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 1, 11, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 3, 12, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL),
  (7.0, 7.0, 6.6, 'b', 3, 13, CURRENT_TIMESTAMP, NULL, 'VuLH26', NULL);

