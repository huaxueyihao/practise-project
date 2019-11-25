drop table if exists sys_user;
-- 用户
create table sys_user (
    id bigint(20) primary key auto_increment,
    user_name varchar(20) not null comment '用户名',
    password varchar(255) not null comment '密码',
    telphone varchar(20) comment '手机',
    sex varchar(1) default '0' comment '0-男，1-女，2-位置',
    age int(3) comment '年龄',
    create_time timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp not NULL default CURRENT_TIMESTAMP
        on update CURRENT_TIMESTAMP comment '修改时间'
) default charset=utf8 ;
-- 菜单
drop table if exists sys_menu;
create table sys_menu (
    id bigint(20) primary key auto_increment,
    menu_name varchar(20) not null comment '菜单名',
    parent_id bigint(20) not null default 0 comment '菜单父id',
    route_url varchar(100) not null comment '菜单路由',
    little_icon varchar (20) comment '小图标',
    target varchar(20) comment '打开方式：_self:本窗口打开,blank:新窗口打开 ',
    leaf_node smallint (1) default 1 comment '是否叶子节点，0:不是，1：是',
    menu_type int(1) default 1 comment '类型，0:根目录，1:菜单，2：按钮',
    create_time timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp not NULL default CURRENT_TIMESTAMP
        on update CURRENT_TIMESTAMP comment '修改时间'
) default charset=utf8 ;

-- 文件
drop table if exists sys_file;
create table sys_file (
    id bigint(20) primary key auto_increment,
    file_name varchar(100) not null comment '文件名',
    path varchar(100) not null comment '文件路劲',
    extension varchar(10) not null comment '文件扩展名',
    module varchar (20) comment '模块',
    moduleId bigint(20) comment '模块主键Id',
    file_size bigint (20) default 0 comment '文件大小',
    remark varchar (255) comment '备注',
    create_time timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp not NULL default CURRENT_TIMESTAMP
        on update CURRENT_TIMESTAMP comment '修改时间'
)  default charset=utf8 ;


-- 计划类型
drop table if exists sys_plan_type;
create table sys_plan_type (
    id bigint(20) primary key auto_increment,
    user_id bigint(20) not null comment '用户id',
    type_name varchar(100) not null comment '类型名称唯一',
    type_desc varchar(100) not null comment '类型描述',
    icon varchar(20)  comment '小图标',
    bg_color varchar (20) comment '颜色',
    remark varchar (100) comment '备注',
    create_time timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp not NULL default CURRENT_TIMESTAMP
        on update CURRENT_TIMESTAMP comment '修改时间'
)  default charset=utf8 ;


-- 日计划执行表
drop table if exists sys_plan_day_execution;
create table sys_plan_day_execution (
    id bigint(20) primary key auto_increment,
    user_id bigint(20) not null comment '用户id',
    title varchar(100) not null comment '主题',
    type_name varchar(100) not null comment '类型名称唯一',
    type_desc varchar(100) not null comment '类型描述',
    start_time datetime not null comment '开始时间：年月日时分秒',
    end_time datetime not null comment '结束时间：年月日时分秒',
    plan_state varchar(1) default 1 comment '状态，1：计划，2：执行，3：暂停，4，延后：5：完成，',
    use_time bigint(10) comment '耗时',
    remark varchar (100) comment '说明',
    location varchar (100) comment '位置',
    create_time timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp not NULL default CURRENT_TIMESTAMP
        on update CURRENT_TIMESTAMP comment '修改时间'
)  default charset=utf8 ;


-- 角色

drop table if exists sys_role;
create table sys_role (
    id bigint(20) primary key auto_increment,
    role_name varchar(100) not null comment '角色名称',
    role_desc varchar(100)   comment '角色描述',
    remark varchar(100)  comment '备注',
    create_time timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp not NULL default CURRENT_TIMESTAMP
        on update CURRENT_TIMESTAMP comment '修改时间'
)  default charset=utf8 ;


drop table if exists sys_user_role;
create table sys_user_role (
    user_id bigint(20) not null comment '用户id',
    role_id bigint(20) not null comment '角色id',
    PRIMARY key (user_id,role_id)
)  default charset=utf8 ;


drop table if exists sys_permission;
create table sys_permission (
    id bigint(20) primary key auto_increment,
    perm_name varchar(100) not null comment '权限名称',
    perm_desc varchar(100)  comment '权限描述',
    remark varchar(100)  comment '备注',
    create_time timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp not NULL default CURRENT_TIMESTAMP
        on update CURRENT_TIMESTAMP comment '修改时间'
)  default charset=utf8 ;


drop table if exists sys_role_perm;
create table sys_role_perm (
    role_id bigint(20) not null comment '角色id',
    perm_id bigint(20) not null comment '权限id',
    PRIMARY key (role_id,perm_id)
)  default charset=utf8 ;


