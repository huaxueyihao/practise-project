drop table if exists sys_user;
create table sys_user (
    id int primary key auto_increment,
    user_name varchar(20) not null comment '用户名',
    password varchar(32) not null comment '密码',
    sex char(1) default '0' comment '0-男，1-女，2-位置',
    age int(3) comment '年龄'
);
