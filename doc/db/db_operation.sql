CREATE DATABASE `db_operation`;

USE `db_operation`;

CREATE TABLE `activity` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `pid` int unsigned NOT NULL DEFAULT '0' COMMENT '主活动ID(主子活动，支持两级)',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '活动名称',
  `theme` int unsigned NOT NULL DEFAULT '0' COMMENT '活动主题',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `property` varchar(128) NOT NULL DEFAULT '' COMMENT '活动属性JSON',
  `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '上架状态',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营活动';

INSERT INTO `activity` (`id`,`create_time`,`update_time`,`title`,`theme`,`start_time`,`end_time`,`property`,`status`,`remark`)
VALUES (1,'2022-12-02 19:12:06','2022-12-02 19:12:06','好运星活动',1,'2022-12-01 00:00:00','2022-12-31 23:59:59','{"fullScore":5000}',1,'积分活动示例：1) 用户选择礼品完成许愿，在prize_record中创建一条待抽奖的任务记录；2）用户通过完成积分任务获取足够的积分；3）用户使用积分兑换心愿礼品');

CREATE TABLE `task` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '任务标题',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营活动id',
  `event_type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '触发任务的行为类型',
  `task_type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '任务类型',
  `task_period` varchar(128) NOT NULL DEFAULT '' COMMENT '任务约束条件：任务周期频率JSON',
  `property` varchar(255) NOT NULL DEFAULT '' COMMENT '任务属性JSON',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_task_activityid` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营任务';

INSERT INTO `task` (`id`,`create_time`,`update_time`,`title`,`activity_id`,`event_type`,`task_type`,`task_period`,`property`,`remark`)
VALUES (1,'2022-12-02 19:12:16','2022-12-02 19:12:16','每日签到',1,200,2,'{"unit":4,"time":1,"number":1}','{"rewards":[{"coefficient":1,"scoreType":0,"scoreValue":100},{"coefficient":2,"scoreType":0,"scoreValue":200},{"coefficient":3,"scoreType":0,"scoreValue":300}]}','每日签到赠送积分。第一天签到送100，连续签到两天送200，连续签到三天送300,签到4天送300,以此类推');
INSERT INTO `task` (`id`,`create_time`,`update_time`,`title`,`activity_id`,`event_type`,`task_type`,`task_period`,`property`,`remark`)
VALUES (2,'2022-12-02 19:12:16','2022-12-02 19:12:16','邀请好友助力',1,201,1,'{"unit":4,"time":1,"number":3}','{"rewards":[{"scoreType":0,"scoreValue":800}]}','分享中奖记录给好友，邀请好友成功参与活动，给邀请者赠送积分');
INSERT INTO `task` (`id`,`create_time`,`update_time`,`title`,`activity_id`,`event_type`,`task_type`,`task_period`,`property`,`remark`)
VALUES (3,'2022-12-02 19:12:16','2022-12-02 19:12:16','完善个人信息',1,1,4,'','{"rewards":[{"scoreType":0,"scoreValue":1000}]}','完善个人必填信息，赠送且只送一次积分');

CREATE TABLE `task_object` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `task_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营任务id',
  `object_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '任务约束条件：对象类型',
  `object_value` varchar(255) NOT NULL DEFAULT '' COMMENT '任务约束条件：对象值',
  PRIMARY KEY (`id`),
  KEY `idx_taskobject_taskid` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营任务对象约束条件';

CREATE TABLE `prize` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '活动id',
  `prize_name` varchar(64) NOT NULL DEFAULT '' COMMENT '奖品名称',
  `prize_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '奖品类型',
  `third_prize_value` varchar(100) NOT NULL DEFAULT '' COMMENT '第三方奖品值',
  `prize_image` varchar(100) NOT NULL DEFAULT '' COMMENT '奖品图片',
  `total_inventory` int unsigned NOT NULL DEFAULT 0 COMMENT '总库存，小于0（-1）时，不做库存校验',
  `probability` int unsigned NOT NULL DEFAULT 0 COMMENT '中奖概率/100000',
  `consume_inventory` int unsigned NOT NULL DEFAULT 0 COMMENT '已消耗库存',
  `lock_inventory` int unsigned NOT NULL DEFAULT 0 COMMENT '已锁定库存',
  `sequence` int unsigned NOT NULL DEFAULT 0 COMMENT '奖品排列顺序',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_prize_activityid` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营活动奖品';

CREATE TABLE `webpage` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营活动id',
  `terminal_ype` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '用户终端类型',
  `code` text NULL COMMENT '源码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_webpage_activityid_terminaltype` (`activity_id`, `terminal_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动网页模板';

CREATE TABLE `prize_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营活动id',
  `activity_pid` int unsigned NOT NULL DEFAULT 0 COMMENT '主活动ID',
  `task_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营任务id',
  `prize_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营奖品id',
  `object_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '对象类型',
  `object_value` varchar(100) NOT NULL DEFAULT '' COMMENT '对象值',
  `lock_status` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '锁定状态',
  `receive_status` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '领取状态',
  `receive_time` timestamp NULL DEFAULT NULL COMMENT '领取时间',
  `user_id` int NOT NULL DEFAULT 0 COMMENT '中奖人id',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '运营任务完成时间（任务记录创建时间）',
  `idempotent_key` varchar(100) NOT NULL DEFAULT '' COMMENT '幂等KEY',
  `prize_name` varchar(64) NOT NULL DEFAULT '' COMMENT '奖品名称',
  `prize_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '奖品类型',
  `third_prize_value` varchar(100) NOT NULL DEFAULT '' COMMENT '第三方奖品值',
  `prize_image` varchar(100) NOT NULL DEFAULT '' COMMENT '奖品图片',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_prizerecord_idempotentkey` (`idempotent_key`),
  KEY `idx_prizerecord_activityid_userid` (`activity_id`, `user_id`),
  KEY `idx_prizerecord_activitypid_userid` (`activity_pid`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抽奖任务记录';

CREATE TABLE `score_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '领取人id',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营活动id',
  `activity_pid` int unsigned NOT NULL DEFAULT 0 COMMENT '主活动ID',
  `task_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营任务id',
  `object_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '对象类型',
  `object_value` varchar(100) NOT NULL DEFAULT '' COMMENT '对象值',
  `score_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '积分类型',
  `score_value` int unsigned NOT NULL DEFAULT 0 COMMENT '积分值',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '任务完成时间',
  `idempotent_key` varchar(100) NOT NULL DEFAULT '' COMMENT '幂等KEY',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_scorerecord_idempotentkey` (`idempotent_key`),
  KEY `idx_scorerecord_activityid_userid` (`activity_id`, `user_id`),
  KEY `idx_scorerecord_activitypid_userid` (`activity_pid`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分任务记录';

CREATE TABLE `prize_code` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营活动id',
  `prize_id` int unsigned NOT NULL DEFAULT 0 COMMENT '奖品id(兑奖码类型的奖品)',
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '编码',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '领取人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prizecode_prizeid_code` (`prize_id`, `code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑奖码';

CREATE TABLE `invite_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '活动id',
  `activity_pid` int unsigned NOT NULL DEFAULT 0 COMMENT '主活动ID',
  `task_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营任务id',
  `inviter_id` int unsigned NOT NULL DEFAULT 0 COMMENT '邀请人id',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '被邀请人id',
  `object_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '对象类型',
  `object_value` varchar(100) NOT NULL DEFAULT '' COMMENT '对象值',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '任务完成时间',
  `idempotent_key` varchar(100) NOT NULL DEFAULT '' COMMENT '幂等KEY',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_inviterecord_idempotentkey` (`idempotent_key`),
  KEY `idx_inviterecord_activityid_inviterid` (`activity_id`, `inviter_id`),
  KEY `idx_inviterecord_activitypid_inviterid` (`activity_pid`, `inviter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邀请记录';

CREATE TABLE `sign_log` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '用户id',
  `continuous_count` int unsigned NOT NULL DEFAULT 0 COMMENT '连续签到天数',
  `total_count` int unsigned NOT NULL DEFAULT 0 COMMENT '累计签到天数',
  `sign_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`id`),
  KEY `idx_signlog_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签到日志';

CREATE TABLE `prize_deliver` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `prize_record_id` int unsigned NOT NULL DEFAULT 0 COMMENT '奖品记录id',
  `user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `phone` varchar(16) NOT NULL DEFAULT '' COMMENT '电话',
  `region_code` varchar(16) NOT NULL DEFAULT '' COMMENT '地区编码',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '详细地址',
  `order_num` varchar(16) NOT NULL DEFAULT '' COMMENT '订单号',
  `deliver_time` timestamp NULL DEFAULT NULL COMMENT '交付时间',
  `fr_id` int unsigned NOT NULL DEFAULT 0 COMMENT '交付负责人员工id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prizedeliver_prizerecordid` (`prize_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='奖品交付';

CREATE TABLE `init_log` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `activity_id` int unsigned NOT NULL DEFAULT 0 COMMENT '运营活动id',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '用户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_preprocesslog_activityid_userid` (`activity_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='初始化日志';
