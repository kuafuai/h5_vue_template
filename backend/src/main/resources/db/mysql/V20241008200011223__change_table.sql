drop table if exists `change_manager`;
create table `change_manager`
(
    `change_id`            int          NOT NULL AUTO_INCREMENT,
    `change_title`         varchar(255) NOT NULL,
    `change_type`          varchar(255) NOT NULL,
    `change_customer`      varchar(255) DEFAULT NULL,
    `change_project_name`  varchar(255) DEFAULT NULL,
    `change_product_name`  varchar(255) DEFAULT NULL,
    `change_project_stage` varchar(255) DEFAULT '',
    `change_start_time`    datetime     DEFAULT NULL,
    `change_end_time`      datetime     DEFAULT NULL,
    `change_person`        int          DEFAULT NULL,
    `change_person_name`   varchar(255) DEFAULT NULL,
    `flowable_instance_id` varchar(255) DEFAULT NULL,
    `change_status`        int          DEFAULT '1',
    `part_number`          varchar(255) DEFAULT '',
    PRIMARY KEY (`change_id`)
);