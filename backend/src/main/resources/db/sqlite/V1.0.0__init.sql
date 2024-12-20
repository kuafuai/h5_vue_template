drop table if exists static_resources;
create table if not exists static_resources
(
    resource_id              INTEGER
        primary key autoincrement,
    resource_name            VARCHAR(255) ,
    resource_path            VARCHAR(500) not null,
    related_table_name       VARCHAR(255),
    related_table_key        INTEGER,
    relate_table_column_name varchar(255)
);

drop table if exists category;
create table if not exists category
(
    id                     integer primary key autoincrement, -- id
    name                   varchar(255) not null,             -- 列别名称
    parent_id              integer     ,             -- 父级类别id
    create_time            date,
    update_time            date,
    relevance_table        varchar(255),                      -- 表分类
    relevance_table_column varchar(255)                       -- 分类字段
);



CREATE TABLE system_messages
(
    id        INTEGER PRIMARY KEY autoincrement, -- 主键ID，SQLite 使用 INTEGER PRIMARY KEY AUTOINCREMENT 实现自增
    type      TEXT NOT NULL,                      -- 类型：notification(通知)、carousel(轮播图)、announcement(公告栏)
    title     TEXT,                               -- 标题，NULL 默认为可为空
    content   TEXT,                               -- 内容，适用于通知和公告栏
    image_url TEXT,                               -- 适用于轮播图：图片URL
    url       TEXT                                -- 适用于轮播图：跳转的URL
);