--liquibase formatted sql
--changeset A.Yefriemov:fill-in-movie

-- truncate table movie restart identity ;
insert into movie(id, created_date, modified_date, title, timing, producer)
values (1, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'Interstellar', 75, 'Christopher Nolan'),
       (2, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'Batman Begins', 90, 'Christopher Nolan'),
       (3, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'Oppenheimer', 54, 'Christopher Nolan'),
       (4, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'Transformers', 98, 'Nelson Shin'),
       (5, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'Dark of the Moon', 102, 'Nelson Shin'),
       (6, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'Dunkirk', 102, 'Hz');
alter sequence movie_id_seq restart with 7;
