-- 用户
insert into sys_user (user_name,password,sex,age) values ('admin','$2a$10$4H1fV2bl.ZJxs1y41Mbx7ev6MDCRkCvY98vA6kFKyGkvKfo0uC1WG','0',20);
insert into sys_user (user_name,password,sex,age) values ('韩梅梅','$2a$10$4H1fV2bl.ZJxs1y41Mbx7ev6MDCRkCvY98vA6kFKyGkvKfo0uC1WG','1',18);
insert into sys_user (user_name,password,sex,age) values ('李雷','$2a$10$4H1fV2bl.ZJxs1y41Mbx7ev6MDCRkCvY98vA6kFKyGkvKfo0uC1WG','0',19);



-- 菜单
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('常规管理',0,'','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('组件管理',0,'','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('其他管理',0,'','','_self');


insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('系统管理',1,'','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('菜单管理',2,'/menu/index','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('用户管理',2,'/user/index','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('文件管理',2,'/file/index','','_self');

insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('个人管理',1,'','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('行为类型',6,'/plan/typeIndex','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('年计划',6,'/plan/index','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('季计划',6,'','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('月计划',6,'','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('周计划',6,'','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('行为记录',6,'/plan/dayExecutionIndex2','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('日计划',6,'/plan/dayExecutionIndex','','_self');


-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('阅读管理',1,'','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('书籍分类',12,'','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('阅读小结',12,'','','_self');

insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('个人中心',1,'','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('基本资料',9,'','','_self');








-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('图标列表',2,'/user/index','','_self');
-- insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('图标选择',2,'/user/index','','_self');


-- 文件
insert into sys_file (file_name,path,extension,module,moduleId,file_size) values ('测试1','434','pdf','客户',1,20);
insert into sys_file (file_name,path,extension,module,moduleId,file_size) values ('测试2','434','jpg','客户',1,30);


-- 计划类型

insert into sys_plan_type (type_name,type_desc,bg_color,user_id) values ('SLEEPING','睡眠','#ff5583',1);
insert into sys_plan_type (type_name,type_desc,bg_color,user_id) values ('READING','阅读','#03bd9e',1);
insert into sys_plan_type (type_name,type_desc,bg_color,user_id) values ('EATTING','用餐','#bbdc00',1);
insert into sys_plan_type (type_name,type_desc,bg_color,user_id) values ('SPORT','运动','#9d9d9d',1);


-- 角色
insert into sys_role (role_name,role_desc,remark) values ('SUPPER_SYSTEM_MANAGER','系统管理员','拥有所有权限');
insert into sys_role (role_name,role_desc,remark) values ('FILE_MANAGER','文件管理员','文件相关操作');
insert into sys_role (role_name,role_desc,remark) values ('ORDER_MANAGER','订单管理员','下单相关的操作');


-- 用户角色关系表

insert into sys_user_role (user_id,role_id) values (1,1);
insert into sys_user_role (user_id,role_id) values (1,2);
insert into sys_user_role (user_id,role_id) values (1,3);

insert into sys_user_role (user_id,role_id) values (2,2);
insert into sys_user_role (user_id,role_id) values (3,3);

-- 权限表
insert into sys_permission (perm_name,perm_desc,remark) values ('/file/index','文件管理首页');
insert into sys_permission (perm_name,perm_desc,remark) values ('/menu/index','菜单管理首页');








