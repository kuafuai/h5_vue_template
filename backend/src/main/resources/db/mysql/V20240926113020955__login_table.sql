drop table if exists `login`;
create table `login` (
                         login_id        INT      not null AUTO_INCREMENT,
                         relevance_id    VARCHAR(255) ,
                         password        VARCHAR(255) ,
                         user_name       VARCHAR(255) ,
                         relevance_table VARCHAR(255) ,
                         phone_number    VARCHAR(255) ,
                         wx_open_id      VARCHAR(255) ,
                         primary key (login_id)
);