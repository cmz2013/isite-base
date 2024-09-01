CREATE DATABASE `db_user`;

USE `db_user`;

CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户登录名',
  `real_name` varchar(32) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `phone` varchar(32) NOT NULL DEFAULT '' COMMENT '手机号',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '密码',
  `email` varchar(128) NOT NULL DEFAULT '' COMMENT 'email地址',
  `status` tinyint unsigned DEFAULT NULL COMMENT '状态',
  `internal` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '系统内置：1是，0否',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name` (`user_name`),
  UNIQUE KEY `uk_user_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

INSERT INTO `user` VALUES (1,'admin','管理员','15010029113','$2a$10$xRH1cpLKVfgrWkDFzCsonuOt2OVMx9n54Pd5CQgIjAX/Ma8OCH.nC','zhangcm@glodon.com',1,1,'系统内置','2025-01-01 09:00:00','2025-01-01 00:00:00');

CREATE TABLE `consignee` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '用户id',
  `user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `phone` varchar(16) NOT NULL DEFAULT '' COMMENT '电话',
  `region_code` varchar(16) NOT NULL DEFAULT '' COMMENT '地区编码',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '详细地址',
  `defaults` tinyint NOT NULL DEFAULT 0 COMMENT '默认地址',
  PRIMARY KEY (`id`),
  KEY `idx_consignee_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收件人';

CREATE TABLE `vip` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '用户id',
  `expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '到期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vip_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='VIP会员';