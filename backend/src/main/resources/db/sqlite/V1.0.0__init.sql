drop table if exists static_resources;
CREATE TABLE static_resources
(
    resource_id        integer  PRIMARY KEY autoincrement , -- 资源ID，自增主键
    resource_name      VARCHAR(255) NOT NULL,          -- 资源名称
    resource_path      VARCHAR(500) NOT NULL,          -- 资源路径
    related_table_name VARCHAR(255),                   -- 关联表名称，可以为空
    related_table_key  integer                             -- 关联表主键ID，可以为空
);