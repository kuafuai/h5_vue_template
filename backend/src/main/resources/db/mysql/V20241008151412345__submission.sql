drop table if exists `submission_material`;
create table `submission_material`
(
    submission_id   INT          not null AUTO_INCREMENT,
    submission_name VARCHAR(255) not null,
    submission_type      INT,
    submission_user_id   VARCHAR(255),
    submission_user_name VARCHAR(255),
    primary key (submission_id)
);