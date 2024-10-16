drop table if exists `deploy_form`;
CREATE TABLE `deploy_form`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `form_id`   bigint(20) DEFAULT NULL COMMENT '表单主键',
    `deploy_id` varchar(50) DEFAULT NULL COMMENT '流程实例主键',
    PRIMARY KEY (`id`) USING BTREE
)