CREATE DATABASE `db_tenant`;

USE `db_tenant`;

CREATE TABLE `tenant` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tenant_name` varchar(128) NOT NULL DEFAULT '' COMMENT '租户名称',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `contact` varchar(32) NOT NULL DEFAULT '' COMMENT '联系人',
  `phone` varchar(32) NOT NULL DEFAULT '' COMMENT '联系电话',
  `expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '到期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户';

CREATE TABLE `department` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `department_name` varchar(32) NOT NULL DEFAULT '' COMMENT '部门名称',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '状态',
  `pid` int unsigned NOT NULL DEFAULT 0 COMMENT '父节点id(根节点的父ID为0）',
  `pids` varchar(255) NOT NULL DEFAULT '' COMMENT '所有父节点ID，逗号分隔，从根节点到子节点有序',
  `tenant_id` int unsigned NOT NULL DEFAULT 0 COMMENT '租户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_department_tenantid` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门';

CREATE TABLE `employee` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tenant_id` int unsigned NOT NULL DEFAULT 0 COMMENT '租户ID',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '用户ID',
  `domain_account` varchar(32) NOT NULL DEFAULT '' COMMENT '员工域账号',
  `dept_id` int unsigned NOT NULL DEFAULT 0 COMMENT '部门ID',
  `office_status` tinyint NOT NULL DEFAULT 0 COMMENT '员工岗位状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_tenantid_userid` (`tenant_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工';

CREATE TABLE `role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) NOT NULL DEFAULT '' COMMENT '角色名称',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `tenant_id` int unsigned NOT NULL DEFAULT 0 COMMENT '租户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_tenantid` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色（基于角色的访问控制 RBAC：Role-Based policies Access Control）';

CREATE TABLE `resource` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `pid` int unsigned NOT NULL DEFAULT 0 COMMENT '父节点id(根节点的父ID为0）',
  `pids` varchar(255) NOT NULL DEFAULT '' COMMENT '所有父节点ID，逗号分隔，从根节点到子节点有序',
  `resource_name` varchar(64) NOT NULL DEFAULT '' COMMENT '资源名称',
  `type` varchar(8) NOT NULL DEFAULT '' COMMENT '资源类型，文件夹 folder, 页面 page, 按钮 btn',
  `icon` varchar(128) NOT NULL DEFAULT '' COMMENT '图标',
  `href` varchar(128) NOT NULL DEFAULT '' COMMENT 'url地址',
  `sort` int unsigned NOT NULL DEFAULT 0 COMMENT '菜单排序',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `client_id` varchar(32) NOT NULL DEFAULT '' COMMENT '终端ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_resource_clientid` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源';

INSERT INTO `resource` VALUES
(1,0,'','系统','folder','','',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(101,1,'1','数据字典','page','','',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(102,1,'1','文件导出','page','','',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(103,1,'1','标签','folder','','',3,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(10301,103,'1,103','标签分类','page','','',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(10302,103,'1,103','标签管理','page','','',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(10303,103,'1,103','打标签','page','','',3,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),

(2,0,'','用户中心','folder','','',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(201,2,'2','用户管理','page','','',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(202,2,'2','收货人','page','','',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),

(3,0,'','租户','folder','','',3,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(301,3,'3','系统资源','page','','',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(302,3,'3','数据接口','page','','',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(303,3,'3','企业管理','page','','',3,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(304,3,'3','部门管理','page','','',4,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(305,3,'3','角色管理','page','','',5,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(306,3,'3','员工管理','page','','',6,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),

(4,0,'','数据集成','folder','','',4,'','data.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(401,4,'4','运行报表','page','','/report.html',1,'','data.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(402,4,'4','执行器','page','','/executor.html',2,'','data.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(403,4,'4','数据接口','page','','/api.html',3,'','data.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(404,4,'4','日志','page','','/log.html',4,'','data.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(405,4,'4','用户','page','','/user.html',5,'','data.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),

(5,0,'','考试','folder','','',5,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(501,5,'5','题库管理','page','','/exam-pool.html',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(502,5,'5','题目管理','page','','/question.html',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(503,5,'5','试卷管理','page','','/exam-paper.html',3,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
-- 考试场景：关联试卷
(504,5,'5','考试场景','page','','/exam-scene.html',4,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(505,5,'5','考试记录','page','','/exam-record.html',5,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),

(6,0,'','产品运营','folder','','',6,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(601,6,'6','运营活动','page','','/activity.html',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(602,6,'6','运营奖品','page','','/prize.html',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(603,6,'6','兑奖码','page','','/prize-code.html',3,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(604,6,'6','运营任务','page','','/task.html',4,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(605,6,'6','活动页面','page','','/webpage.html',5,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(606,6,'6','任务记录','folder','','',6,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(60601,606,'6,606','领奖记录','page','','/prize-record.html',1,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(60602,606,'6,606','积分记录','page','','/score.html',2,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(60603,606,'6,606','邀请记录','page','','/invite-record.html',3,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(607,6,'6','签到记录','page','','/sign.html',6,'','system.browser','2025-01-01 00:00:00','2025-01-01 00:00:00')
;

CREATE TABLE `data_api` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `service_id` varchar(64) NOT NULL COMMENT '服务ID',
  `method` varchar(32) NOT NULL DEFAULT '' COMMENT 'Http Method',
  `request_path` varchar(128) NOT NULL COMMENT '接口路径',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据接口（仅配置需要授权的接口。用户登录以后，如果没有授权不能访问）';

INSERT INTO `data_api` VALUES
(101,'data-admin','GET','/data/executors','','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(102,'data-admin','GET','/data/apis','','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(103,'data-admin','GET','/data/executor/{serviceId}','','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(104,'data-admin','GET','/data/logs','','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(105,'data-admin','PUT','/data/log/{logId}/retry','','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(106,'data-admin','GET','/data/report','','2025-01-01 00:00:00','2025-01-01 00:00:00'),
(107,'data-admin','PUT','/data/api/{id}/status/{status}','','2025-01-01 00:00:00','2025-01-01 00:00:00');

CREATE TABLE `resource_api` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `resource_id` int unsigned NOT NULL DEFAULT 0,
  `api_id` int unsigned NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_resourceapi_resourceid` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口权限';

INSERT INTO `resource_api` VALUES
(1,401,101,'2025-01-01 00:00:00','2025-01-01 00:00:00'),
(2,402,102,'2025-01-01 00:00:00','2025-01-01 00:00:00'),(3,402,107,'2025-01-01 00:00:00','2025-01-01 00:00:00'),
(4,403,104,'2025-01-01 00:00:00','2025-01-01 00:00:00'),(5,403,105,'2025-01-01 00:00:00','2025-01-01 00:00:00'),
(6,404,106,'2025-01-01 00:00:00','2025-01-01 00:00:00'),
(7,405,108,'2025-01-01 00:00:00','2025-01-01 00:00:00');

CREATE TABLE `role_resource` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `resource_id` int unsigned NOT NULL DEFAULT 0 COMMENT '资源ID',
  `role_id` int unsigned NOT NULL DEFAULT 0 COMMENT '角色ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_roleresource_roleid` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源';

CREATE TABLE `employee_role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int unsigned NOT NULL DEFAULT 0 COMMENT '员工ID',
  `role_id` int unsigned NOT NULL DEFAULT 0 COMMENT '角色ID',
  `tenant_id` int unsigned NOT NULL DEFAULT 0 COMMENT '租户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_employeerole_employeeid` (`employee_id`),
  KEY `idx_employeerole_tenantid` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工角色';
