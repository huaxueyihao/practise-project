drop table if exists sys_user;
-- 用户
create table sys_user (
    id bigint(20) primary key auto_increment,
    user_name varchar(20) not null comment '用户名',
    password varchar(32) not null comment '密码',
    telphone varchar(20) comment '手机',
    sex varchar(1) default '0' comment '0-男，1-女，2-位置',
    age int(3) comment '年龄'
);
-- 菜单
drop table if exists sys_menu;
create table sys_menu (
    id bigint(20) primary key auto_increment,
    menu_name varchar(20) not null comment '菜单名',
    parent_id bigint(20) not null default 0 comment '菜单父id',
    route_url varchar(20) not null comment '菜单路由',
    little_icon varchar (20) comment '小图标'
);
