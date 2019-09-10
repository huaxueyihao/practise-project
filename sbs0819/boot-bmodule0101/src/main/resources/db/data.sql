-- 用户
insert into sys_user (user_name,password,sex,age) values ('admin','123456','0',20);
insert into sys_user (user_name,password,sex,age) values ('韩梅梅','123456','1',18);
insert into sys_user (user_name,password,sex,age) values ('李雷','123456','0',19);



-- 菜单
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('常规管理',0,'','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('组件管理',0,'','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('其他管理',0,'','','_self');


insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('系统管理',1,'','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('菜单管理',4,'/menu/index','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('用户管理',4,'/user/index','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('文件管理',4,'/file/index','','_self');

insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('图标列表',2,'/user/index','','_self');
insert into sys_menu (menu_name,parent_id,route_url,little_icon,target) values ('图标选择',2,'/user/index','','_self');


-- 文件
insert into sys_file (file_name,path,extension,module,moduleId,file_size) values ('测试1','434','pdf','客户',1,20);
insert into sys_file (file_name,path,extension,module,moduleId,file_size) values ('测试2','434','jpg','客户',1,30);


