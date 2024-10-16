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



drop table if exists category;
create table if not exists category
(
    id                     integer primary key autoincrement, -- id
    name                   varchar(255) not null,             -- 列别名称
    parent_id              integer      not null,             -- 父级类别id
    create_time            date,
    update_time            date,
    relevance_table        varchar(255),                      -- 表分类
    relevance_table_column varchar(255)                       -- 分类字段
    );




