drop table if exists `change_manager`;
create table `change_manager`
(
    change_id   INT          not null AUTO_INCREMENT,
    change_title VARCHAR(255) not null,
    change_type VARCHAR(255) not null,
    change_customer   VARCHAR(255),
    change_project_name   VARCHAR(255),
    change_product_name   VARCHAR(255),
    change_start_time   datetime,
    change_end_time   datetime,
    change_person   INT,
    change_person_name   VARCHAR(255),
    flowable_instance_id   VARCHAR(255),
    primary key (change_id)
);