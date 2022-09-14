--liquibase formatted sql
--changeset A.Yefriemov:create_order

alter table if exists order_table
    drop constraint order_table_time_table_fk;
alter table if exists order_table
    add constraint order_table_time_table_fk foreign key (time_table_id) references time_table (id) on DELETE cascade on update cascade;
