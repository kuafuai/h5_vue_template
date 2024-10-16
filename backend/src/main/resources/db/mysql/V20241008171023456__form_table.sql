drop table if exists `form_setting`;
create table `form_setting`
(
    form_id   INT          not null AUTO_INCREMENT,
    form_name VARCHAR(255) not null,
    form_description   VARCHAR(255),
    form_content VARCHAR(2000),
    primary key (form_id)
);