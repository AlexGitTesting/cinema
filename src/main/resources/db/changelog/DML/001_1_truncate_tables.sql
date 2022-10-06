--liquibase formatted sql
--changeset A.Yefriemov:truncate_tables

truncate table order_table restart identity;
truncate table time_table restart identity cascade;
truncate table cinema_hall restart identity cascade;
truncate table movie restart identity cascade;