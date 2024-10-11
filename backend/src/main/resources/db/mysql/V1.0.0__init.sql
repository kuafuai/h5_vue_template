show tables;
CREATE TABLE static_resources (
                                  resource_id INT AUTO_INCREMENT PRIMARY KEY, -- 资源ID，自增主键
                                  resource_name VARCHAR(255) NOT NULL,        -- 资源名称
                                  resource_path VARCHAR(500) NOT NULL,        -- 资源路径
                                  related_table_name VARCHAR(255),            -- 关联表名称，可以为空
                                  related_table_key INT                       -- 关联表主键ID，可以为空
);



