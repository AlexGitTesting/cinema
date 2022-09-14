--liquibase formatted sql
--changeset A.Yefriemov:fill-in-cinema-hall

-- truncate table cinema_hall restart identity ;
insert into cinema_hall(id, created_date, modified_date, name, seats_amount, seats_type)
values (1, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'RED', 10, '{
  "BLIND": [
    1,
    2,
    3
  ],
  "KISSES": [
    4,
    5,
    6
  ],
  "LUXURY": [
    7,
    8,
    9,
    10
  ]
}'),
       (2, '2022-09-14 00:04:36.215', '2022-09-14 00:04:36.215', 'BLUE', 12, '{
         "BLIND": [
           1,
           2,
           3,
           4
         ],
         "KISSES": [
           5,
           6,
           7,
           8
         ],
         "LUXURY": [
           9,
           10,
           11,
           12
         ]
       }');
alter sequence cinema_hall_id_seq restart with 3;