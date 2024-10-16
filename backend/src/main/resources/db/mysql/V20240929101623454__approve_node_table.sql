drop table if exists `approve_node`;
create table `approve_node`
(
    approve_node_id   INT          not null AUTO_INCREMENT,
    approve_node_name VARCHAR(255) not null,
    approve_type      INT,
    approve_user_id   VARCHAR(255),
    approve_user_name VARCHAR(255),
    primary key (approve_node_id)
);