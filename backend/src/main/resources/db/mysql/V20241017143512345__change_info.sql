drop table if exists `change_manager_sub`;
CREATE TABLE `change_manager_sub`
(
    `sub_task_id`        int          NOT NULL AUTO_INCREMENT,
    `parent_proc_ins_id` varchar(255) NOT NULL,
    `parent_task_id`     varchar(255) NOT NULL,
    `sub_proc_ins_id`    varchar(255) NOT NULL,
    PRIMARY KEY (`sub_task_id`)
);


drop table if exists `change_manager_info`;
CREATE TABLE `change_manager_info`
(
    `change_info_id` int NOT NULL AUTO_INCREMENT,
    `change_id`      int          DEFAULT NULL,
    `proc_ins_id`    varchar(255) DEFAULT NULL,
    `info_key`       varchar(255) DEFAULT NULL,
    `info_value`     text,
    `create_time`    datetime     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`change_info_id`)
);