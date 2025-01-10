
drop table if exists `change_follow`;
CREATE TABLE `change_follow`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `follow_person_id` int,
    `follow_change_id`  int,
    `follow_time`       datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) COMMENT='关注';