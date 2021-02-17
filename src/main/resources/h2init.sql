DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;


CREATE TABLE IF NOT EXISTS students
(
    id   bigint AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200)
    );

CREATE TABLE IF NOT EXISTS teachers
(
    id   bigint AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200)
    );

