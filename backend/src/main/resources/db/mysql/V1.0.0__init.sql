show tables;
drop table if exists static_resources;
create table static_resources
(
    resource_id              int
        primary key autoincrement,
    resource_name            VARCHAR(255) not null,
    resource_path            VARCHAR(500) not null,
    related_table_name       VARCHAR(255),
    related_table_key        int,
    relate_table_column_name varchar(255)
);



