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



insert into order_table (id, created_date, time_table_id, order_price, seats, customer)
values (100, create_order_booking_time(1000), 1000, count_total_price(1000), '[
  3,
  5,
  10
]', 'customer'),
       (101, create_order_booking_time(1001), 1001, count_total_price(1001), '[
         2,
         3
       ]', 'customer'),
       (102, create_order_booking_time(1001), 1001, count_total_price(1001), '[
         6,
         7,
         10
       ]', 'customer'),
       (103, create_order_booking_time(1002), 1002, count_total_price(1002), '[
         3,
         6
       ]', 'customer'),
       (104, create_order_booking_time(1002), 1002, count_total_price(1002), '[
         9,
         10
       ]', 'customer'),
       (105, create_order_booking_time(1003), 1003, count_total_price(1003), '[
         5,
         4
       ]', 'customer'),
       (106, create_order_booking_time(1004), 1004, count_total_price(1004), '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (107, create_order_booking_time(1004), 1004, count_total_price(1004), '[
         9,
         10,
         5,
         7,
         8
       ]', 'customer'),
       (108, create_order_booking_time(1005), 1005, count_total_price(1005), '[
         1,
         2,
         3
       ]', 'customer'),
       (109, create_order_booking_time(1006), 1006, count_total_price(1006), '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', 'customer'),
       (110, create_order_booking_time(1007), 1007, count_total_price(1007), '[
         1,
         2
       ]', 'customer'),
       (111, create_order_booking_time(1007), 1007, count_total_price(1007), '[
         6,
         7
       ]', 'customer'),
       (112, create_order_booking_time(1008), 1008, count_total_price(1008), '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (113, create_order_booking_time(1008), 1008, count_total_price(1008), '[
         5,
         7,
         8,
         9
       ]', 'customer'),
       (114, create_order_booking_time(1008), 1008, count_total_price(1008), '[
         10,
         11,
         12
       ]', 'customer'),
-- second day

       (115, create_order_booking_time(1009), 1009, count_total_price(1009), '[
         3,
         5,
         10
       ]', 'customer'),
       (116, create_order_booking_time(1010), 1010, count_total_price(1010), '[
         2,
         3
       ]', 'customer'),
       (117, create_order_booking_time(1010), 1010, count_total_price(1010), '[
         6,
         7,
         10
       ]', 'customer'),
       (118, create_order_booking_time(1011), 1011, count_total_price(1011), '[
         3,
         6
       ]', 'customer'),
       (119, create_order_booking_time(1011), 1011, count_total_price(1011), '[
         9,
         10
       ]', 'customer'),
       (120, create_order_booking_time(1012), 1012, count_total_price(1012), '[
         5,
         4
       ]', 'customer'),
       (121, create_order_booking_time(1013), 1013, count_total_price(1013), '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (122, create_order_booking_time(1013), 1013, count_total_price(1013), '[
         9,
         10,
         5,
         7,
         8
       ]', 'customer'),
       (123, create_order_booking_time(1014), 1014, count_total_price(1014), '[
         1,
         2,
         3
       ]', 'customer'),
       (124, create_order_booking_time(1015), 1015, count_total_price(1015), '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', 'customer'),
       (125, create_order_booking_time(1016), 1016, count_total_price(1016), '[
         1,
         2
       ]', 'customer'),
       (126, create_order_booking_time(1016), 1016, count_total_price(1016), '[
         6,
         7
       ]', 'customer'),
       (127, create_order_booking_time(1017), 1017, count_total_price(1017), '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (128, create_order_booking_time(1017), 1017, count_total_price(1017), '[
         5,
         7,
         8,
         9
       ]', 'customer'),
       (129, create_order_booking_time(1017), 1017, count_total_price(1017), '[
         10,
         11,
         12
       ]', 'customer'),
-- third
       (130, create_order_booking_time(1018), 1018, count_total_price(1018), '[
         3,
         5,
         10
       ]', 'customer'),
       (131, create_order_booking_time(1019), 1019, count_total_price(1019), '[
         2,
         3
       ]', 'customer'),
       (132, create_order_booking_time(1019), 1019, count_total_price(1019), '[
         6,
         7,
         10
       ]', 'customer'),
       (133, create_order_booking_time(1020), 1020, count_total_price(1020), '[
         3,
         6
       ]', 'customer'),
       (134, create_order_booking_time(1020), 1020, count_total_price(1020), '[
         9,
         10
       ]', 'customer'),
       (135, create_order_booking_time(1021), 1021, count_total_price(1021), '[
         5,
         4
       ]', 'customer'),
       (136, create_order_booking_time(1022), 1022, count_total_price(1022), '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (137, create_order_booking_time(1022), 1022, count_total_price(1022), '[
         9,
         10,
         5,
         7,
         8
       ]', 'customer'),
       (138, create_order_booking_time(1023), 1023, count_total_price(1023), '[
         1,
         2,
         3
       ]', 'customer'),
       (139, create_order_booking_time(1024), 1024, count_total_price(1024), '[
         3,
         6,
         7,
         8,
         9,
         10
       ]', 'customer'),
       (140, create_order_booking_time(1025), 1025, count_total_price(1025), '[
         1,
         2
       ]', 'customer'),
       (141, create_order_booking_time(1025), 1025, count_total_price(1025), '[
         6,
         7
       ]', 'customer'),
       (142, create_order_booking_time(1026), 1026, count_total_price(1026), '[
         1,
         2,
         3,
         4,
         6
       ]', 'customer'),
       (143, create_order_booking_time(1026), 1026, count_total_price(1026), '[
         5,
         7,
         8,
         9
       ]', 'customer'),
       (144, create_order_booking_time(1026), 1026, count_total_price(1026), '[
         10,
         11,
         12
       ]', 'customer'),
--        night performance
       (145, create_order_booking_time(1027), 1027, count_total_price(1027), '[
         3,
         6,
         9,
         10
       ]', 'customer'),
       (146, create_order_booking_time(1028), 1028, count_total_price(1028), '[
         3,
         6,
         9,
         10
       ]', 'customer'),
       (147, create_order_booking_time(1029), 1029, count_total_price(1029), '[
         3,
         6,
         9,
         10
       ]', 'customer'),


       --for test see timetable maybe need to insert for production
       (148, create_order_booking_time(1030), 1030, count_total_price(1030), '[
         2,
         3,
         6
       ]', 'customer'),
       (149, create_order_booking_time(1030), 1030, count_total_price(1030), '[
         7,
         10
       ]', 'customer');


