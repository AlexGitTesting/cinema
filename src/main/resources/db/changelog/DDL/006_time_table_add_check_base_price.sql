--liquibase formatted sql
--changeset A.Yefriemov:alter_time_table_check_base_price

alter table if exists time_table
    add constraint time_table_base_price_check check ( base_price >= 0 );