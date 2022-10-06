--liquibase formatted sql
--changeset A.Yefriemov:fill-in-time-table

insert into time_table(id, movie_id, cinema_hall_id, start_session, base_price, closed_seats, sold)
values (1, 2, 1, CURRENT_DATE + interval '9' hour, 50, '[
  3,
  5,
  10
]', false),
       (2, 1, 2, CURRENT_DATE + interval '9' hour, 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false),
       (3, 3, 1, CURRENT_DATE + time '10:50:00', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (4, 4, 2, CURRENT_DATE + time '10:35:00', 75, '[
         5,
         4
       ]', false),
       (5, 5, 1, CURRENT_DATE + time '12:10:00', 75, '[
         1,
         2,
         3,
         4,
         5,
         6,
         7,
         8,
         9,
         10
       ]', true),
       (6, 2, 2, CURRENT_DATE + time '12:30:00', 75, '[
         1,
         2,
         3
       ]', false),
       (7, 4, 1, CURRENT_DATE + time '14:40:00', 75, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', false),
       (8, 1, 2, CURRENT_DATE + time '14:20:00', 75, '[
         1,
         2,
         6,
         7
       ]', false),
       (9, 3, 2, CURRENT_DATE + time '16:30:00', 75, '[
         1,
         2,
         3,
         4,
         5,
         6,
         7,
         8,
         9,
         10,
         11,
         12
       ]', true),

       (10, 2, 1, CURRENT_DATE + interval '1 day' + time ' 09:00:00', 50, '[
         3,
         5,
         10
       ]', false),
       (11, 1, 2, CURRENT_DATE + interval '1 day' + time ' 09:00:00', 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false),
       (12, 3, 1, CURRENT_DATE + interval '1 day' + time ' 10:50:00', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (13, 4, 2, CURRENT_DATE + interval '1 day' + time ' 10:35:00', 75, '[
         5,
         4
       ]', false),
       (14, 5, 1, CURRENT_DATE + interval '1 day' + time ' 12:10:00', 75, '[
         1,
         2,
         3,
         4,
         5,
         6,
         7,
         8,
         9,
         10
       ]', false),
       (15, 2, 2, CURRENT_DATE + interval '1 day' + time ' 12:30:00', 75, '[
         1,
         2,
         3
       ]', false),
       (16, 4, 1, CURRENT_DATE + interval '1 day' + time ' 14:40:00', 75, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', false),
       (17, 1, 2, CURRENT_DATE + interval '1 day' + time ' 14:20:00', 75, '[
         1,
         2,
         6,
         7
       ]', false),
       (18, 3, 2, CURRENT_DATE + interval '1 day' + time ' 16:30:00', 75, '[
         1,
         2,
         3,
         4,
         5,
         6,
         7,
         8,
         9,
         10,
         11,
         12
       ]', true),

       (19, 2, 1, CURRENT_DATE + interval '2 day' + time ' 09:00:00', 50, '[
         3,
         5,
         10
       ]', false),
       (20, 1, 2, CURRENT_DATE + interval '2 day' + time ' 09:00:00', 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false),
       (21, 3, 1, CURRENT_DATE + interval '2 day' + time ' 10:50:00', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (22, 4, 2, CURRENT_DATE + interval '2 day' + time ' 10:35:00', 75, '[
         5,
         4
       ]', false),
       (23, 5, 1, CURRENT_DATE + interval '2 day' + time ' 12:10:00', 75, '[
         1,
         2,
         3,
         4,
         5,
         6,
         7,
         8,
         9,
         10
       ]', true),
       (24, 2, 2, CURRENT_DATE + interval '2 day' + time ' 12:30:00', 75, '[
         1,
         2,
         3
       ]', false),
       (25, 4, 1, CURRENT_DATE + interval '2 day' + time ' 14:40:00', 75, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', false),
       (26, 1, 2, CURRENT_DATE + interval '2 day' + time ' 14:20:00', 75, '[
         1,
         2,
         6,
         7
       ]', false),
       (27, 3, 2, CURRENT_DATE + interval '2 day' + time ' 16:30:00', 75, '[
         1,
         2,
         3,
         4,
         5,
         6,
         7,
         8,
         9,
         10,
         11,
         12
       ]', true),

--       *********** night performance
       (28, 3, 1, CURRENT_DATE + time '23:50:59', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (29, 3, 2, CURRENT_DATE + interval '1 day' + time '23:50:59', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (30, 3, 1, CURRENT_DATE + interval '2 day' + time '23:50:59', 75, '[
         3,
         6,
         9,
         10
       ]', false),


       --for test only
       (31, 1, 2, current_timestamp - interval '3' hour, 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false);
alter sequence time_table_id_seq restart with 32;
