show tables;
DROP TABLE IF EXISTS static_resources;
CREATE TABLE IF NOT EXISTS static_resources
(
    resource_id              INT PRIMARY KEY AUTO_INCREMENT, -- 设置为自增
    resource_name            VARCHAR(255) NOT NULL,
    resource_path            VARCHAR(500) NOT NULL,
    related_table_name       VARCHAR(255) DEFAULT NULL,
    related_table_key        INT DEFAULT NULL,
    relate_table_column_name VARCHAR(255) DEFAULT NULL
    );




DROP TABLE IF EXISTS category;
CREATE TABLE IF NOT EXISTS category
(
    id                     INT PRIMARY KEY AUTO_INCREMENT, -- id
    name                   VARCHAR(255) NOT NULL,          -- 类别名称
    parent_id              INT DEFAULT NULL,               -- 父级类别id
    create_time            DATETIME DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time            DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
    relevance_table        VARCHAR(255) DEFAULT NULL,      -- 表分类
    relevance_table_column VARCHAR(255) DEFAULT NULL       -- 分类字段
    );





