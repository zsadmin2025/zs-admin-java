-- 菜单sql --
INSERT INTO `sys_menu` (`pid`,`title`,`path`,`type`,`name`,`component`,`icon`,`link`,`requires_auth`,`hide_in_menu`,`hide_children_in_menu`,`active_menu`,`visible`,`sort`,`no_affix`,`ignore_cache`,`permissions`,`status`,`creator`,`create_time`,`updater`,`update_time`)
VALUES ('${parentMenuId?c}', '${functionName}','/${moduleName}/${businessName}',2,'${moduleName}/${businessName}','/${moduleName}/${businessName}/index','icon-outlined-file-text',NULL,1,0,0,NULL,1,1,0,0,'',1,1633833655917559809,sysdate(),null,null);

-- 保存最后插入的 ID
SET @last_menu_id = LAST_INSERT_ID();


-- 按钮 SQL
insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}分页',  '3',  '1', '${moduleName}:${businessName}:page', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}列表',  '3',  '2', '${moduleName}:${businessName}:list', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}新增',  '3',  '3', '${moduleName}:${businessName}:save', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}删除',  '3',  '4', '${moduleName}:${businessName}:delete', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}修改',  '3',  '5', '${moduleName}:${businessName}:update', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}详情',  '3',  '6', '${moduleName}:${businessName}:info', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}批量删除',  '3',  '7', '${moduleName}:${businessName}:batchDelete', '1', 1633833655917559809, sysdate(), null, null);

insert into sys_menu (`pid`, `title`, `type`, `sort`, `permissions`, `status`,`creator`,`create_time`,`updater`,`update_time`)
values(@last_menu_id, '${functionName}导出',  '3',  '8', '${moduleName}:${businessName}:export', '1', 1633833655917559809, sysdate(), null, null);