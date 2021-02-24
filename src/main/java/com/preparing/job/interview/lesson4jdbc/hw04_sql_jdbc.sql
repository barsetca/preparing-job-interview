CREATE
DATABASE movie_scheduler;

use
movie_scheduler;

DROP table movies;
/*
У фильма, который идет в кинотеатре, есть название, длительность (пусть будет 60, 90 или 120 минут),
цена билета (в разное время и дни может быть разной),
время начала сеанса (один фильм может быть показан несколько раз в разное время и с разной ценой билета).
*/
CREATE TABLE movies
(
    `id`         BIGINT         NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(120)   NOT NULL,
    `date`       DATE           NOT NULL,
    `start_time` TIME           NOT NULL,
    `duration`   TIME           NOT NULL,
    `price`      DECIMAL(11, 2) NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `movie_scheduler`.`movies`
    ADD UNIQUE INDEX `idx_name_start_time` (`start_time` ASC, `name` ASC, `date` ASC) VISIBLE;

INSERT INTO movies (name, date, start_time, duration, price)
VALUES ('Alternative', '2000-12-12', '10:00:00', '1:30', 800),
       ('News', '2000-12-12', '11:00:00', '2:00', 1000),
       ('Function', '2000-12-12', '15:00:00', '1:00', 600),
       ('Scheduler', '2000-12-12', '18:00:00', '1:30', 700),
       ('Big', '2000-12-12', '20:00:00', '2:00', 1200),
       ('Big', '2000-12-12', '21:00:00', '2:00', 1200),
       ('Alternativa', '2000-12-12', '19:00:00', '1:30', 800);

/*
Есть информация о купленных билетах (номер билета, на какой сеанс).
*/

CREATE TABLE tickets
(
    `id`       BIGINT NOT NULL AUTO_INCREMENT,
    `movie_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX      `movie_id_idx` (`movie_id` ASC) VISIBLE,
    CONSTRAINT `movie_id`
        FOREIGN KEY (`movie_id`)
            REFERENCES `movies` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

INSERT INTO tickets (movie_id)
VALUES (1), (2), (3), (4), (5),
       (6), (1), (1), (3), (4),
       (6), (6), (1), (1), (1),
       (1), (5), (4), (2), (2),
       (2), (4), (5), (6), (7);

/*
ошибки в расписании (фильмы накладываются друг на друга), отсортированные по возрастанию времени.
Выводить надо колонки «фильм 1», «время начала», «длительность», «фильм 2», «время начала», «длительность»;

*/
SELECT m1.name as 'фильм 1',
       m1.start_time,
       m1.duration,
       m2.name as 'фильм 2',
       m2.start_time,
       m2.duration
FROM movies m1
         JOIN movies m2 ON m1.date = m2.date
WHERE m1.id != m2.id AND (m2.start_time BETWEEN m1.start_time AND (m1.start_time + m1.duration))
ORDER BY m1.start_time ASC;

/*
'фильм 1    | start_time | duration  | фильм 2      | start_time | duration
Alternative	    10:00:00	 01:30:00	       News	    11:00:00	02:00:00
Scheduler	    18:00:00	 01:30:00	Alternativa	    19:00:00	01:30:00
Alternative	    19:00:00	 01:30:00	        Big	    20:00:00	02:00:00
Big	            20:00:00	 02:00:00	        Big	    21:00:00	02:00:00
*/

/*
перерывы 30 минут и более между фильмами — выводить по уменьшению длительности перерыва.
Колонки «фильм 1», «время начала», «длительность», «время начала второго фильма», «длительность перерыва»;

*/
SELECT m1.name as 'фильм 1',
       m1.start_time,
       m1.duration,
       m2.name as 'фильм 2',
       m2.start_time,
       MIN(TIMESTAMPDIFF(minute, TIME(m1.start_time + m1.duration), m2.start_time)) as break_minute
FROM movies m1
         JOIN movies m2 ON m1.date = m2.date
WHERE m1.id != m2.id AND ((m1.start_time + m1.duration) < m2.start_time)
GROUP BY m1.start_time
HAVING break_minute >=30
ORDER BY break_minute DESC;

/*
фильм 1       | start_time | duration | фильм 2    | start_time | break_minute
Alternative	   10:00:00	    01:30:00   Function	    15:00:00	 210
News	       11:00:00	    02:00:00   Function	    15:00:00	 120
Function	   15:00:00	    01:00:00   Scheduler    18:00:00	 120
Scheduler	   18:00:00	    01:30:00   Big	        20:00:00	 30
Alternative	   19:00:00	    01:30:00   Big	        21:00:00	 30
*/

/*
список фильмов, для каждого — с указанием общего числа посетителей за все время,
среднего числа зрителей за сеанс и общей суммы сборов по каждому фильму (отсортировать по убыванию прибыли).
Внизу таблицы должна быть строчка «итого», содержащая данные по всем фильмам сразу;

*/

CREATE
TEMPORARY TABLE film_name
SELECT movies.name as film_name, COUNT(*) as count, SUM(movies.price) as sum
from movies
    JOIN tickets
ON movies.id = tickets.movie_id
GROUP BY film_name
ORDER BY sum DESC;

CREATE
TEMPORARY TABLE start_time
SELECT movies.name, movies.start_time, COUNT(*) as count
from movies
    JOIN tickets
ON movies.id = tickets.movie_id
GROUP BY movies.start_time;

CREATE
TEMPORARY TABLE avg_count
SELECT name, AVG(count) as avg_ticket
FROM start_time
GROUP BY name;

drop
temporary table film_name;
drop
temporary table start_time;
drop
temporary table avg_count;

/*
SELECT name, AVG(count1) as avg_ticket FROM (SELECT movies.name, movies.start_time, COUNT(*) as count1 from movies
JOIN tickets ON movies.id = tickets.movie_id GROUP BY movies.start_time) as my GROUP BY name;
*/

CREATE
TEMPORARY TABLE for_itogs
SELECT one.film_name, one.count, one.sum, avg_ticket
FROM film_name as one
         JOIN avg_count as two ON one.film_name = two.name;

/*
CREATE TEMPORARY TABLE for_itogs
SELECT one.film_name, one.count, one.sum, avg_ticket FROM (SELECT movies.name as film_name, COUNT(*) as count, SUM(movies.price) as sum from movies
JOIN tickets ON movies.id = tickets.movie_id GROUP BY film_name ORDER BY sum DESC) as one
JOIN (SELECT name, AVG(count1) as avg_ticket FROM (SELECT movies.name, movies.start_time, COUNT(*) as count1 from movies
left JOIN tickets ON movies.id = tickets.movie_id GROUP BY movies.start_time) as my GROUP BY name) as two ON one.film_name = two.name;
*/


SELECT film_name, count, sum, avg_ticket
FROM film_name as one
         JOIN avg_count as two ON one.film_name = two.name
UNION
select 'ИТОГ' film_name, SUM(count), SUM(sum), AVG(avg_ticket)
from for_itogs;
/*
film_name | coun | sum       | avg_ticket
Alternative	   8	 6400.00	4.00000000
News	       4	 4000.00	4.00000000
Function	   2	 1200.00	2.00000000
Scheduler	   4	 2800.00	4.00000000
Big	           7	 8400.00	3.50000000
ИТОГ	      25	 22800.00	3.50000000
*/

drop
temporary table for_itogs;

/*
SELECT one.film_name, SUM(one.count), SUM(one.sum), AVG(avg_ticket) FROM (SELECT movies.name as film_name, COUNT(*) as count, SUM(movies.price) as sum from movies
JOIN tickets ON movies.id = tickets.movie_id GROUP BY film_name) as one
JOIN (SELECT name, AVG(count1) as avg_ticket FROM (SELECT movies.name, movies.start_time, COUNT(*) as count1 from movies
left JOIN tickets ON movies.id = tickets.movie_id GROUP BY movies.start_time) as my GROUP BY name) as two ON one.film_name = two.name;
*/

/*
число посетителей и кассовые сборы, сгруппированные по времени начала фильма:
с 9 до 15, с 15 до 18, с 18 до 21, с 21 до 00:00 (сколько посетителей пришло с 9 до 15 часов и т.д.).
*/

CREATE
TEMPORARY TABLE time_9
SELECT movies.start_time, COUNT(*) as count, SUM(price) as sum
from movies
    JOIN tickets
ON movies.id = tickets.movie_id
WHERE start_time BETWEEN '9:00:00' AND '14:59:59'
GROUP BY movies.start_time;

CREATE
TEMPORARY TABLE time_15
SELECT movies.start_time, COUNT(*) as count, SUM(price) as sum
from movies
    JOIN tickets
ON movies.id = tickets.movie_id
WHERE start_time BETWEEN '15:00:00' AND '17:59:59'
GROUP BY movies.start_time;

CREATE
TEMPORARY TABLE time_18
SELECT movies.start_time, COUNT(*) as count, SUM(price) as sum
from movies
    JOIN tickets
ON movies.id = tickets.movie_id
WHERE start_time BETWEEN '18:00:00' AND '20:59:59'
GROUP BY movies.start_time;

CREATE
TEMPORARY TABLE time_21
SELECT movies.start_time, COUNT(*) as count, SUM(price) as sum
from movies
    JOIN tickets
ON movies.id = tickets.movie_id
WHERE start_time BETWEEN '21:00:00' AND '23:59:59'
GROUP BY movies.start_time;

select 'c 9 до 15' start_time, SUM(count), SUM(sum)
from time_9
UNION
select 'c 15 до 18' start_time, SUM(count), SUM(sum)
from time_15
UNION
select 'c 18 до 21' start_time, SUM(count), SUM(sum)
from time_18
UNION
select 'c 21 до 00' start_time, SUM(count), SUM(sum)
from time_21;

/*
start_time | SUM(count) | SUM(sum)
c 9 до 15	          11	9600.00
c 15 до 18	           2	1200.00
c 18 до 21	           8	7200.00
c 21 до 00	           4	4800.00
*/

drop
temporary table time_9;
drop
temporary table time_15;
drop
temporary table time_18;
drop
temporary table time_21;