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
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
);



create table if not exists curriculum.categories
(
    id           SERIAL PRIMARY KEY,
    name         varchar(255) not null UNIQUE,
    subject_type varchar(255),
    create_time  timestamp(6) not null,
    create_by    varchar(255) not null,
    update_time  timestamp(6),
    update_by    varchar(255)
);


create table if not exists curriculum.courses
(
    id          SERIAL PRIMARY KEY,
    code        varchar(15)  not null UNIQUE,
    name        varchar(255) not null,
    credits     integer      not null,
    description varchar(255) not null,
    start_day   date         not null,
    start_time  date         not null,
    category_id bigint       not null
        constraint fkkyes7515s3ypoovxrput029bh
            references curriculum.categories,
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
);

create table if not exists curriculum.curriculums
(
    iid           SERIAL PRIMARY KEY,
    name          varchar(255) not null,
    year          integer      not null,
    total_credits smallint     not null,
    create_time   timestamp(6) not null,
    update_time   timestamp(6),
    create_by     varchar(255) not null,
    update_by     varchar(255)
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
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
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
    create_time   timestamp(6) not null,
    update_time   timestamp(6),
    create_by     varchar(255) not null,
    update_by     varchar(255)
);

create table if not exists faculty.classes
(
    id          SERIAL PRIMARY KEY,
    code        varchar(255) not null UNIQUE,
    name        varchar(255) not null,
    faculty_id  bigint
        constraint fkpoymrbf5df7x6k2ylbxfbgaqu
            references faculty.faculties,
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
);


create table if not exists student.guardians
(
    id            SERIAL PRIMARY KEY,
    name          varchar(255) not null,
    phone         varchar(255) not null,
    relationships varchar(255) not null,
    create_time   timestamp(6) not null,
    update_time   timestamp(6),
    create_by     varchar(255) not null,
    update_by     varchar(255)
);

create table if not exists curriculum.prerequisites
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255),
    description varchar(255),
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
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
    create_time   timestamp(6) not null,
    create_by     varchar(255) not null,
    update_time   timestamp(6),
    update_by     varchar(255)
);


create table if not exists salary.salary
(
    id          SERIAL PRIMARY KEY,
    base_salary integer      not null,
    pay_date    date         not null,
    create_time timestamp(6) not null,
    update_time timestamp(6),
    create_by   varchar(255) not null,
    update_by   varchar(255)
);

create table if not exists faculty.instructors
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code        varchar(20) not null UNIQUE,
    name        varchar(50) not null,
    dob         date         not null,
    email       varchar(100) not null UNIQUE,
    phone       varchar(11) not null UNIQUE,
    gender      varchar(10) not null,
    address     varchar(255) not null,
    account_id  UUID,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account.accounts(id),
    faculty_id  bigint
        constraint fki8vktt5q456toh0o8apn8o8rf
            references faculty.faculties,
    salary_id   bigint
        constraint fkbfp539ioos6am107yk50dcugg
            references salary.salary,
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
);

create table if not exists faculty.course_instructor
(
    id            SERIAL PRIMARY KEY,
    course_id     bigint not null
        constraint fkeqej22fgwa29i98ucd9x9ycie
            references curriculum.courses,
    instructor_id UUID   not null
        constraint fkltori8qni3ivrlovca4gd9yw3
            references faculty.instructors
);

create table if not exists department.managers
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code          varchar(255) not null UNIQUE,
    name          varchar(255) not null,
    dob           date         not null,
    email         varchar(255) not null UNIQUE,
    phone         varchar(255) not null UNIQUE,
    gender        varchar(255) not null,
    address       varchar(255) not null,
    account_id    UUID
        unique
        constraint fks8vxfog0lwxdn09g7d71fkuxp
            references account.accounts,
    department_id bigint
        constraint fk78bxps40sjq2glpliqw79ewqv
            references department.departments,
    salary_id     bigint
        constraint fkm1sveer9f5qktxvx464tl7e5x
            references salary.salary,
    create_time   timestamp(6) not null,
    create_by     varchar(255) not null,
    update_time   timestamp(6),
    update_by     varchar(255)
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
    instructor_id UUID         not null
        constraint fk8l1v75brk9afixg1li5ymt7nj
            references faculty.instructors,
    room_id       bigint       not null
        constraint fkoyfccms1psubki0cm7c92msrp
            references building.rooms,
    create_time   timestamp(6) not null,
    create_by     varchar(255) not null,
    update_time   timestamp(6),
    update_by     varchar(255)
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
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
);

create table if not exists curriculum.course_semester
(
    id          SERIAL PRIMARY KEY,
    course_id   bigint not null
        constraint fkqo728e9f9aj1bne0m40nbkuwo
            references curriculum.courses,
    semester_id bigint not null
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
    instructor_id   UUID         not null
        constraint fk23nklkf992jpo1hxqmpa16t77
            references faculty.instructors,
    semester_id     bigint       not null
        constraint fkp8njc4xlmts6sdw7p3ra2shkf
            references curriculum.semester,
    create_time     timestamp(6) not null,
    create_by       varchar(255) not null,
    update_time     timestamp(6),
    update_by       varchar(255)
);

create table if not exists timetable.timetable
(
    id          SERIAL PRIMARY KEY,
    day_of_week varchar(255),
    semester_id bigint       not null
        constraint fkmkvrn4nnpjhpjxgqvd4dhfdbl
            references curriculum.semester,
    create_time timestamp(6) not null,
    create_by   varchar(255) not null,
    update_time timestamp(6),
    update_by   varchar(255)
);

create table if not exists student.students
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code          varchar(255) not null UNIQUE,
    name          varchar(255) not null,
    dob           date         not null,
    email         varchar(255) not null UNIQUE,
    phone         varchar(255) not null UNIQUE,
    gender        varchar(255) not null,
    address       varchar(255) not null,
    academic_year varchar(255) not null,
    account_id    UUID
        unique
        constraint fkoootcgotavmpat2yv9o52wx1q
            references account.accounts,
    class_id      bigint
        constraint fknsl7w2nw6o6eq53hqlxfcijpm
            references faculty.classes,
    guardian_id   bigint
        constraint fkkktpamdaydbva4kxjcn17sbvk
            references student.guardians,
    timetable_id  bigint       not null
        constraint fk7yw3t9woq5rcao21u1a625l8t
            references timetable.timetable,
    create_time   timestamp(6) not null,
    create_by     varchar(255) not null,
    update_time   timestamp(6),
    update_by     varchar(255)
);

create table if not exists student.grade_report
(
    id               SERIAL PRIMARY KEY,
    point_process    double precision not null,
    point_end_course double precision not null,
    total_mark       double precision not null,
    grades           varchar(255),
    instructor_id    UUID             not null
        constraint fkqkyw5ljmjiemlr82rcy5iictj
            references faculty.instructors,
    student_id       UUID             not null
        constraint fknleqou2lxe9cqllvw26k9r709
            references student.students,
    create_time      timestamp(6)     not null,
    create_by        varchar(255)     not null,
    update_time      timestamp(6),
    update_by        varchar(255)
);
create table if not exists student.student_course
(
    id         SERIAL PRIMARY KEY,
    course_id  bigint not null
        constraint fkejrkh4gv8iqgmspsanaji90ws
            references curriculum.courses,
    student_id UUID not null
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
    student_id UUID             not null
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

