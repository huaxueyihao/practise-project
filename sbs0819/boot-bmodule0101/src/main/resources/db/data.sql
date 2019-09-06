-- 用户
insert into sys_user (user_name,password,sex,age) values ('admin','123456','0',20);
insert into sys_user (user_name,password,sex,age) values ('韩梅梅','123456','1',18);
insert into sys_user (user_name,password,sex,age) values ('李雷','123456','0',19);
insert into sys_user (user_name,password,sex,age) values ('李雷1','123456','0',19);
insert into sys_user (user_name,password,sex,age) values ('李雷2','123456','0',19);
insert into sys_user (user_name,password,sex,age) values ('李雷3','123456','0',19);
insert into sys_user (user_name,password,sex,age) values ('李雷4','123456','0',19);
insert into sys_user (user_name,password,sex,age) values ('李雷5','123456','0',19);
insert into sys_user (user_name,password,sex,age) values ('李雷6','123456','0',19);


-- 菜单
insert into sys_menu (menu_name,parent_id,route_url,little_icon) values ('主页',0,'/dashboard','');
insert into sys_menu (menu_name,parent_id,route_url,little_icon) values ('系统管理',0,'','');
insert into sys_menu (menu_name,parent_id,route_url,little_icon) values ('用户管理',2,'/user/index','');
insert into sys_menu (menu_name,parent_id,route_url,little_icon) values ('菜单管理',2,'/menu/index','');



