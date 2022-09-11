--liquibase formatted sql
--changeset A.Yefriemov:fill-in-movie

insert into movie(title, timing, producer)
values ('Batman Begins', 90, 'Christopher Nolan'),
       ('Interstellar', 75, 'Christopher Nolan'),
       ('Oppenheimer', 54, 'Christopher Nolan'),
       ('Transformers', 98, 'Nelson Shin'),
       ('Dark of the Moon', 102, 'Nelson Shin');
