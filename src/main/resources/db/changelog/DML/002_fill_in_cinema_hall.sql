--liquibase formatted sql
--changeset A.Yefriemov:fill-in-cinema-hall

insert into cinema_hall(name, seats_amount, seats_type)
values ('RED', 10, '{
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
       ('BLUE', 12, '{
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