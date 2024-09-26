drop table if exists `user_info`;
create table `user_info` (
         user_info_id    INT      not null AUTO_INCREMENT,
         user_name       VARCHAR(255) not null,
         department_name    VARCHAR(255),
         user_id VARCHAR(255),
         open_userid VARCHAR(255),
         admin INT,
         admin_read_only INT,
         change_person INT,
         primary key (user_info_id)
);