--liquibase formatted sql
--changeset A.Yefriemov:fill-in-order-table

create or replace function count_total_price(time_table_id integer) returns bigint as
'select jsonb_array_length(m.closed_seats) * m.base_price
 from time_table as m
 where m.id = time_table_id;
'language sql;

create or replace function create_order_booking_time(time_table_idx bigint) returns timestamp as
'select t.start_session - interval ''3'' hour
 from time_table as t
 where t.id = time_table_idx;
' language sql;

create or replace function count_total_price_two(order_id integer) returns bigint as
'select jsonb_array_length(o.seats) * tt.base_price
 from order_table as o
          join time_table tt on o.time_table_id = tt.id and o.id = order_id;
'language sql;



insert into order_table (id, created_date, time_table_id, order_price, seats, customer)
values (1, create_order_booking_time(1), 1, 3, '[
  3,
  5,
  10
]', 'customer'),
       (2, create_order_booking_time(2), 2, 3, '[
         2,
         3
       ]', 'customer'),
       (3, create_order_booking_time(2), 2, 3, '[
         6,
         7,
         10
       ]', 'customer'),
       (4, create_order_booking_time(3), 3, 3, '[
         3,
         6
       ]', 'customer'),
       (5, create_order_booking_time(3), 3, 3, '[
         9,
         10
       ]', 'customer'),
       (6, create_order_booking_time(4), 4, 3, '[
         5,
         4
       ]', 'customer'),
       (7, create_order_booking_time(5), 5, 3, '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (8, create_order_booking_time(5), 5, 3, '[
         9,
         10,
         5,
         7,
         8
       ]', 'customer'),
       (9, create_order_booking_time(6), 6, 3, '[
         1,
         2,
         3
       ]', 'customer'),
       (10, create_order_booking_time(7), 7, 3, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', 'customer'),
       (11, create_order_booking_time(8), 8, 3, '[
         1,
         2
       ]', 'customer'),
       (12, create_order_booking_time(8), 8, 3, '[
         6,
         7
       ]', 'customer'),
       (13, create_order_booking_time(9), 9, 3, '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (14, create_order_booking_time(9), 9, 3, '[
         5,
         7,
         8,
         9
       ]', 'customer'),
       (15, create_order_booking_time(9), 9, 3, '[
         10,
         11,
         12
       ]', 'customer'),
-- second day

       (16, create_order_booking_time(10), 10, 3, '[
         3,
         5,
         10
       ]', 'customer'),
       (17, create_order_booking_time(11), 11, 3, '[
         2,
         3
       ]', 'customer'),
       (18, create_order_booking_time(11), 11, 3, '[
         6,
         7,
         10
       ]', 'customer'),
       (19, create_order_booking_time(12), 12, 3, '[
         3,
         6
       ]', 'customer'),
       (20, create_order_booking_time(12), 12, 3, '[
         9,
         10
       ]', 'customer'),
       (21, create_order_booking_time(13), 13, 3, '[
         5,
         4
       ]', 'customer'),
       (22, create_order_booking_time(14), 14, 3, '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (23, create_order_booking_time(14), 14, 3, '[
         9,
         10,
         5,
         7,
         8
       ]', 'customer'),
       (24, create_order_booking_time(15), 15, 3, '[
         1,
         2,
         3
       ]', 'customer'),
       (25, create_order_booking_time(16), 16, 3, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', 'customer'),
       (26, create_order_booking_time(17), 17, 3, '[
         1,
         2
       ]', 'customer'),
       (27, create_order_booking_time(17), 17, 3, '[
         6,
         7
       ]', 'customer'),
       (28, create_order_booking_time(18), 18, 3, '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (29, create_order_booking_time(18), 18, 3, '[
         5,
         7,
         8,
         9
       ]', 'customer'),
       (30, create_order_booking_time(18), 18, 3, '[
         10,
         11,
         12
       ]', 'customer'),
-- third
       (31, create_order_booking_time(19), 19, 3, '[
         3,
         5,
         10
       ]', 'customer'),
       (32, create_order_booking_time(20), 20, 3, '[
         2,
         3
       ]', 'customer'),
       (33, create_order_booking_time(20), 20, 3, '[
         6,
         7,
         10
       ]', 'customer'),
       (34, create_order_booking_time(21), 21, 3, '[
         3,
         6
       ]', 'customer'),
       (35, create_order_booking_time(21), 21, 3, '[
         9,
         10
       ]', 'customer'),
       (36, create_order_booking_time(22), 22, 3, '[
         5,
         4
       ]', 'customer'),
       (37, create_order_booking_time(23), 23, 3, '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (38, create_order_booking_time(23), 23, 3, '[
         9,
         10,
         5,
         7,
         8
       ]', 'customer'),
       (39, create_order_booking_time(24), 24, 3, '[
         1,
         2,
         3
       ]', 'customer'),
       (40, create_order_booking_time(25), 25, 3, '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', 'customer'),
       (41, create_order_booking_time(26), 26, 3, '[
         1,
         2
       ]', 'customer'),
       (42, create_order_booking_time(26), 26, 3, '[
         6,
         7
       ]', 'customer'),
       (43, create_order_booking_time(27), 27, 3, '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (44, create_order_booking_time(27), 27, 3, '[
         5,
         7,
         8,
         9
       ]', 'customer'),
       (45, create_order_booking_time(27), 27, 3, '[
         10,
         11,
         12
       ]', 'customer'),
--        night performance
       (46, create_order_booking_time(28), 28, 3, '[
         3,
         6,
         9,
         10
       ]', 'customer'),
       (47, create_order_booking_time(29), 29, 3, '[
         3,
         6,
         9,
         10
       ]', 'customer'),
       (48, create_order_booking_time(30), 30, 3, '[
         3,
         6,
         9,
         10
       ]', 'customer'),


       --for test see timetable maybe need to insert for production
       (49, create_order_booking_time(31), 31, 3, '[
         2,
         3,
         6
       ]', 'customer'),
       (50, create_order_booking_time(31), 31, 3, '[
         7,
         10
       ]', 'customer');


update order_table as o
set order_price=count_total_price_two(o.id::integer)
where true;

alter sequence time_table_id_seq restart with 51;