insert into time_table(id, movie_id, cinema_hall_id, start_session, base_price, closed_seats, sold)
values (1000, 1001, 100, CURRENT_DATE + interval '9' hour, 50, '[
  3,
  5,
  10
]', false),
       (1001, 1000, 101, CURRENT_DATE + interval '9' hour, 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false),
       (1002, 1002, 100, CURRENT_DATE + time '10:50:00', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (1003, 1003, 101, CURRENT_DATE + time '10:35:00', 75, '[
         5,
         4
       ]', false),
       (1004, 1004, 100, CURRENT_DATE + time '12:10:00', 75, '[
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
       (1005, 1001, 101, CURRENT_DATE + time '12:30:00', 75, '[
         1,
         2,
         3
       ]', false),
       (1006, 1003, 100, CURRENT_DATE + time '14:40:00', 75, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', false),
       (1007, 1000, 101, CURRENT_DATE + time '14:20:00', 75, '[
         1,
         2,
         6,
         7
       ]', false),
       (1008, 1002, 101, CURRENT_DATE + time '16:30:00', 75, '[
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

       (1009, 1001, 100, CURRENT_DATE + interval '1 day' + time ' 09:00:00', 50, '[
         3,
         5,
         10
       ]', false),
       (1010, 1000, 101, CURRENT_DATE + interval '1 day' + time ' 09:00:00', 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false),
       (1011, 1002, 100, CURRENT_DATE + interval '1 day' + time ' 10:50:00', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (1012, 1003, 101, CURRENT_DATE + interval '1 day' + time ' 10:35:00', 75, '[
         5,
         4
       ]', false),
       (1013, 1004, 100, CURRENT_DATE + interval '1 day' + time ' 12:10:00', 75, '[
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
       (1014, 1001, 101, CURRENT_DATE + interval '1 day' + time ' 12:30:00', 75, '[
         1,
         2,
         3
       ]', false),
       (1015, 1003, 100, CURRENT_DATE + interval '1 day' + time ' 14:40:00', 75, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', false),
       (1016, 1000, 101, CURRENT_DATE + interval '1 day' + time ' 14:20:00', 75, '[
         1,
         2,
         6,
         7
       ]', false),
       (1017, 1002, 101, CURRENT_DATE + interval '1 day' + time ' 16:30:00', 75, '[
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

       (1018, 1001, 100, CURRENT_DATE + interval '2 day' + time ' 09:00:00', 50, '[
         3,
         5,
         10
       ]', false),
       (1019, 1000, 101, CURRENT_DATE + interval '2 day' + time ' 09:00:00', 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false),
       (1020, 1002, 100, CURRENT_DATE + interval '2 day' + time ' 10:50:00', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (1021, 1003, 101, CURRENT_DATE + interval '2 day' + time ' 10:35:00', 75, '[
         5,
         4
       ]', false),
       (1022, 1004, 100, CURRENT_DATE + interval '2 day' + time ' 12:10:00', 75, '[
         3,
         4,
         6,
         9,
         10
       ]', false),
       (1023, 1001, 101, CURRENT_DATE + interval '2 day' + time ' 12:30:00', 75, '[
         1,
         2,
         3
       ]', false),
       (1024, 1003, 100, CURRENT_DATE + interval '2 day' + time ' 14:40:00', 75, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', false),
       (1025, 1000, 101, CURRENT_DATE + interval '2 day' + time ' 14:20:00', 75, '[
         1,
         2,
         6,
         7
       ]', false),
       (1026, 1002, 101, CURRENT_DATE + interval '2 day' + time ' 16:30:00', 75, '[
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
       (1027, 1002, 100, CURRENT_DATE + time '23:50:59', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (1028, 1002, 101, CURRENT_DATE + interval '1 day' + time '23:50:59', 75, '[
         3,
         6,
         9,
         10
       ]', false),
       (1029, 1002, 100, CURRENT_DATE + interval '2 day' + time '23:50:59', 75, '[
         3,
         6,
         9,
         10
       ]', false),


       --for test only
       (1030, 1000, 101, current_timestamp - interval '3' hour, 50, '[
         2,
         3,
         6,
         7,
         10
       ]', false);

