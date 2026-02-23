-- 初始化菜单及角色菜单关联数据
-- 仅在 sys_menu 为空时执行（由 DataInitializer 自动触发）

-- =============================================
-- 菜单数据
-- =============================================
INSERT INTO `sys_menu`
  (`id`,`parent_id`,`title`,`path`,`component`,`redirect`,`icon`,`menu_type`,`permission`,`sort`,`visible`,`keep_alive`,`active_menu`,`breadcrumb`,`show_in_tabs`,`affix`,`always_show`,`status`)
VALUES
-- 一级目录
(1,  0,'分析页',   '/analyse', 'Layout','/analyse/index',                     '',              1,'',2, 1,0,'',1,1,0,0,1),
(2,  0,'数据管理', '/data',    'Layout','/data/index',                        'menu-data',     1,'',3, 1,1,'',1,1,0,0,1),
(3,  0,'文件管理', '/file',    'Layout','/file/index',                        'menu-file',     1,'',4, 1,1,'',1,1,0,0,1),
(4,  0,'表单管理', '/form',    'Layout','/form/base',                         'menu-form',     1,'',5, 1,0,'',1,1,0,0,1),
(5,  0,'表格管理', '/table',   'Layout','/table/base',                        'menu-table',    1,'',6, 1,0,'',1,1,0,0,1),
(6,  0,'错误页',   '/error',   'Layout','/error/403',                         'menu-error',    1,'',7, 1,0,'',1,1,0,0,1),
(7,  0,'结果页',   '/result',  'Layout','/result/success',                    'menu-result',   1,'',8, 1,0,'',1,1,0,0,1),
(8,  0,'系统管理', '/system',  'Layout','/system/user',                       'menu-system',   1,'',9, 1,0,'',1,1,0,0,1),
(9,  0,'示例页',   '/demo',    'Layout','/demo/index',                        '',              1,'',15,1,0,'',1,1,0,0,1),
(10, 0,'多级缓存', '/multi',   'Layout','/multi/menu1/menu1-1/menu1-1-1',     'menu-multi',    1,'',11,1,1,'',1,1,0,0,1),
(11, 0,'关于我们', '/about',   'Layout','/about/index',                       '',              1,'',20,1,0,'',1,1,0,0,1),
(13, 0,'详情页',   '/detail',  'Layout','/detail/base',                       'menu-detail',   1,'',6, 1,0,'',1,1,0,0,1),
(14, 0,'文档',     '/document','Layout','',                                   'menu-document', 1,'',14,1,1,'',1,1,0,0,1),
(15, 0,'布局页',   '/layout',  'Layout','/layout/demo1',                      'menu-layout',   1,'',6, 1,0,'',1,1,0,0,1),
(81, 0,'权限测试', '/test',    'Layout','noRedirect',                         'menu-test',     1,'',9, 1,0,'',1,1,0,1,1),

-- 分析页
(101,1,'分析页','/analyse/index','analyse/index','','menu-analyse',2,'',1,1,0,'',0,1,1,0,1),

-- 数据管理
(201,2,'数据管理','/data/index',     'data/main/index',  '','',2,'',0,1,1,'',          0,1,0,0,1),
(202,2,'详情',    '/data/detail/:id','data/detail/index','','',2,'',0,0,0,'/data/index',1,1,0,0,1),
(203,2,'新增',    '/data/form',      'data/form/index',  '','',2,'',0,0,0,'/data/index',1,1,0,0,1),

-- 文件管理
(301,3,'文件管理','/file/index', 'file/main/index',  '','',2,'',0,1,1,'',           0,1,0,0,1),
(302,3,'详情',    '/file/detail','file/detail/index','','',2,'',0,0,0,'/file/index',1,0,0,0,1),

-- 表单管理
(401,4,'基础表单','/form/base',  'form/base/index',  '','icon-park-outline:notes',2,'',0,1,0,'',1,1,0,0,1),
(402,4,'分步表单','/form/step',  'form/step/index',  '','icon-park-outline:notes',2,'',0,1,0,'',1,1,0,0,1),
(403,4,'配置表单','/form/custom','form/custom/index','','icon-park-outline:notes',2,'',0,1,0,'',1,1,0,0,1),
(404,4,'编辑表格','/form/table', 'form/table/index', '','icon-park-outline:notes',2,'',0,1,1,'',1,1,0,0,1),

-- 表格管理
(501,5,'基础表格',  '/table/base',   'table/base/index',   '','icon-park-outline:table-file',2,'',0,1,0,'',1,1,0,0,1),
(502,5,'自定义表格','/table/custom', 'table/custom/index', '','icon-park-outline:table-file',2,'',0,1,0,'',1,1,0,0,1),
(503,5,'配置化表格','/table/custom2','table/custom2/index','','icon-park-outline:table-file',2,'',0,1,0,'',1,1,0,0,1),
(504,5,'弹窗表格',  '/table/dialog', 'table/dialog/index', '','icon-park-outline:table-file',2,'',0,1,0,'',1,1,0,0,1),

-- 错误页
(601,6,'403页','/error/403','error/403','','icon-park-outline:bug',2,'',0,1,0,'',1,1,0,0,1),
(602,6,'404页','/error/404','error/404','','icon-park-outline:bug',2,'',0,1,0,'',1,1,0,0,1),
(603,6,'500页','/error/500','error/500','','icon-park-outline:bug',2,'',0,1,0,'',1,1,0,0,1),

-- 结果页
(701,7,'成功页','/result/success','result/success/index','','icon-park-outline:report',2,'',0,1,0,'',1,1,0,0,1),
(702,7,'失败页','/result/fail',   'result/fail/index',   '','icon-park-outline:report',2,'',0,1,0,'',1,1,0,0,1),

-- 系统管理（仅 admin）
(801,8,'用户管理','/system/user',   'system/user/index',   '','icon-park-outline:setting-config',2,'',1,1,0,'',1,1,0,0,1),
(802,8,'角色管理','/system/role',   'system/role/index',   '','icon-park-outline:setting-config',2,'',2,1,0,'',1,1,0,0,1),
(803,8,'部门管理','/system/dept',   'system/dept/index',   '','icon-park-outline:setting-config',2,'',3,1,0,'',1,1,0,0,1),
(804,8,'菜单管理','/system/menu',   'system/menu/index',   '','icon-park-outline:setting-config',2,'',4,1,0,'',1,1,0,0,1),
(805,8,'字典管理','/system/dict',   'system/dict/index',   '','icon-park-outline:setting-config',2,'',5,1,0,'',1,1,0,0,1),
(806,8,'用户账户','/system/account','system/account/index','','icon-park-outline:setting-config',2,'',6,1,0,'',1,1,0,0,1),

-- 权限测试
(8101,81,'测试页面1','/test/page1','test/page1/index','','icon-park-outline:protect',2,'',0,1,0,'',1,1,0,0,1),
(8102,81,'测试页面2','/test/page2','test/page2/index','','icon-park-outline:protect',2,'',0,1,0,'',1,1,0,0,1),

-- 按钮权限（type=3，属于测试页面2）
(810201,8102,'按钮新增',      '','','','',3,'user:btn:add',   0,1,0,'',1,1,0,0,1),
(810202,8102,'按钮编辑',      '','','','',3,'user:btn:edit',  0,1,0,'',1,1,0,0,1),
(810203,8102,'按钮删除',      '','','','',3,'user:btn:delete',0,1,0,'',1,1,0,0,1),
(810204,8102,'按钮新增(测试)','','','','',3,'test:btn:add',   0,1,0,'',1,1,0,0,1),
(810205,8102,'按钮编辑(测试)','','','','',3,'test:btn:edit',  0,1,0,'',1,1,0,0,1),
(810206,8102,'按钮删除(测试)','','','','',3,'test:btn:delete',0,1,0,'',1,1,0,0,1),

-- 示例页
(901,9,'示例页','/demo/index','demo/index','','menu-example',2,'',0,1,0,'',0,1,0,0,1),

-- 详情页
(1301,13,'基本详情','/detail/base',  'detail/base/index',  '','icon-park-outline:newspaper-folding',2,'',0,1,0,'',1,1,0,0,1),
(1302,13,'普通详情','/detail/senior','detail/senior/index','','icon-park-outline:newspaper-folding',2,'',0,1,0,'',1,1,0,0,1),

-- 文档
(1401,14,'ArcoDesign文档','/document/arco-design-vue',         'document/arco-design-vue/index','','arco',      2,'',0,1,1,'',0,1,0,0,1),
(1402,14,'Vite文档',      '/document/vite',                    'document/vite/index',           '','vite',      2,'',1,1,1,'',0,1,0,0,1),
(1403,14,'项目地址(外链)','https://gitee.com/lin0716/gi-demo', 'https://gitee.com/lin0716/gi-demo','','menu-gitee',2,'',2,1,0,'',1,1,0,0,1),

-- 布局页
(1501,15,'布局1','/layout/demo1','layout/demo1/index','','icon-park-outline:layout-three',2,'',0,1,0,'',1,1,0,0,1),
(1502,15,'布局2','/layout/demo2','layout/demo2/index','','icon-park-outline:layout-three',2,'',0,1,0,'',1,1,0,0,1),
(1503,15,'布局3','/layout/demo3','layout/demo3/index','','icon-park-outline:layout-three',2,'',0,1,0,'',1,1,0,0,1),
(1504,15,'布局4','/layout/demo4','layout/demo4/index','','icon-park-outline:layout-three',2,'',0,1,0,'',1,1,0,0,1),
(1505,15,'布局5','/layout/demo5','layout/demo5/index','','icon-park-outline:layout-three',2,'',0,1,0,'',1,1,0,0,1),

-- 关于我们
(1101,11,'关于项目','/about/index','about/index','','menu-about',2,'',0,1,0,'',0,1,0,0,1),

-- 多级缓存（三级+）
(1001,  10,   'menu1',    '/multi/menu1',                    '',                                     '','icon-park-outline:connection-point',2,'',0,1,1,'',1,1,0,0,1),
(1002,  10,   'menu2',    '/multi/menu2',                    '',                                     '','icon-park-outline:connection-point',2,'',0,1,1,'',1,1,0,0,1),
(10011, 1001, 'menu1-1',  '/multi/menu1/menu1-1',            '',                                     '','icon-park-outline:connection-point',2,'',0,1,0,'',1,1,0,1,1),
(10012, 1001, 'menu1-2',  '/multi/menu1/menu1-2',            'multi/menu1/menu1-2/index',             '','icon-park-outline:connection-point',2,'',0,1,0,'',1,1,0,0,1),
(100111,10011,'menu1-1-1','/multi/menu1/menu1-1/menu1-1-1',  'multi/menu1/menu1-1/menu1-1-1/index',  '','icon-park-outline:connection-point',2,'',0,1,1,'',1,1,0,0,1),
(10021, 1002, 'menu2-1',  '/multi/menu2/menu2-1',            'multi/menu2/menu2-1/index',             '','icon-park-outline:connection-point',2,'',0,1,1,'',1,1,0,0,1),
(10022, 1002, 'menu2-2',  '/multi/menu2/menu2-2',            'multi/menu2/menu2-2/index',             '','icon-park-outline:connection-point',2,'',0,1,0,'',1,1,0,0,1);

-- =============================================
-- 角色菜单关联
-- role_admin (id=1) -> 所有菜单
-- role_user  (id=2) -> 除系统管理(8,801-806)和测试页面1(8101)外的所有菜单
-- =============================================
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES
-- role_admin 全部菜单
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,13),(1,14),(1,15),(1,81),
(1,101),
(1,201),(1,202),(1,203),
(1,301),(1,302),
(1,401),(1,402),(1,403),(1,404),
(1,501),(1,502),(1,503),(1,504),
(1,601),(1,602),(1,603),
(1,701),(1,702),
(1,801),(1,802),(1,803),(1,804),(1,805),(1,806),
(1,8101),(1,8102),
(1,810201),(1,810202),(1,810203),(1,810204),(1,810205),(1,810206),
(1,901),
(1,1001),(1,1002),(1,10011),(1,10012),(1,100111),(1,10021),(1,10022),
(1,1101),
(1,1301),(1,1302),
(1,1401),(1,1402),(1,1403),
(1,1501),(1,1502),(1,1503),(1,1504),(1,1505),
-- role_user 除系统管理和测试页面1外的菜单
(2,1),(2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,9),(2,10),(2,11),(2,13),(2,14),(2,15),(2,81),
(2,101),
(2,201),(2,202),(2,203),
(2,301),(2,302),
(2,401),(2,402),(2,403),(2,404),
(2,501),(2,502),(2,503),(2,504),
(2,601),(2,602),(2,603),
(2,701),(2,702),
(2,8102),
(2,810201),(2,810202),(2,810203),(2,810204),(2,810205),(2,810206),
(2,901),
(2,1001),(2,1002),(2,10011),(2,10012),(2,100111),(2,10021),(2,10022),
(2,1101),
(2,1301),(2,1302),
(2,1401),(2,1402),(2,1403),
(2,1501),(2,1502),(2,1503),(2,1504),(2,1505);
