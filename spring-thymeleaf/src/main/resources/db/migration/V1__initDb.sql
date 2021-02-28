DROP TABLE IF EXISTS students;

CREATE TABLE IF NOT EXISTS students
(
    id    bigint AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    age  INTEGER
);

create table file(
                     id serial primary key,
                     name varchar(200),
                     create_date timestamp,
                     status varchar(50)
);

create table report(
                       id serial primary key,
                       create_date timestamp,
                       send_date timestamp,
                       complete_date timestamp,
                       type varchar(20),
                       number varchar(300),
                       status varchar(50),
                       error_description varchar(300),
                       pvu_msg_id_in varchar(100),
                       pvu_msg_id_out varchar(100)
);

create table mtm_file2report(
                                id serial primary key,
                                file_id integer,
                                report_id integer
);