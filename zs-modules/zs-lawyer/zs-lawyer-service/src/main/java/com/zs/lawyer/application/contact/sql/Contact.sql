-- 菜单sql --
INSERT INTO `sys_menu` (`pid`,`title`,`path`,`type`,`name`,`component`,`icon`,`link`,`requires_auth`,`hide_in_menu`,`hide_children_in_menu`,`active_menu`,`visible`,`sort`,`no_affix`,`ignore_cache`,`permissions`,`status`,`creator`,`create_time`,`updater`,`update_time`)
VALUES ('1926201535723782146', '通讯录联系人表','/application/contact',2,'application/contact','/application/contact/index','icon-outlined-file-text',NULL,1,0,0,NULL,1,1,0,0,'',1,1633833655917559809,sysdate(),null,null);

-- 保存最后插入的 ID
SET @last_menu_id = LAST_INSERT_ID();


-- 按钮 SQL
insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表分页',  '3',  '1', 'application:contact:page', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表列表',  '3',  '2', 'application:contact:list', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表新增',  '3',  '3', 'application:contact:save', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表删除',  '3',  '4', 'application:contact:delete', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表修改',  '3',  '5', 'application:contact:update', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表详情',  '3',  '6', 'application:contact:info', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表批量删除',  '3',  '7', 'application:contact:batchDelete', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '通讯录联系人表导出',  '3',  '8', 'application:contact:export', '1', 1633833655917559809, sysdate(), null, null);
