CREATE DATABASE `db_shop`;

USE `db_shop`;

CREATE TABLE `spu` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `spu_name` varchar(32) NOT NULL DEFAULT '' COMMENT '产品名称',
  `supplier` varchar(32) NOT NULL DEFAULT '' COMMENT '供应商',
  `supplier_param` varchar(500) NOT NULL DEFAULT '' COMMENT '供应商自定义参数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品信息';

INSERT INTO `spu` VALUES
(1,'iSite 旗舰版','tenant-resource','{"expireDays":365,"resourceIds":[]}','2025-01-01 09:00:00','2025-01-01 00:00:00'),
(2,'iSite 企业版','tenant-resource','{"expireDays":365,"resourceIds":[]}','2025-01-01 09:00:00','2025-01-01 00:00:00'),
(3,'iSite 个人版','tenant-resource','{"expireDays":365,"resourceIds":[]}','2025-01-01 09:00:00','2025-01-01 00:00:00'),
(4,'VIP 年卡','user-vip','{"expireDays":365}','2025-01-01 09:00:00','2025-01-01 00:00:00'),
(5,'VIP 季卡','user-vip','{"expireDays":90}','2025-01-01 09:00:00','2025-01-01 00:00:00'),
(6,'VIP 月卡','user-vip','{"expireDays":30}','2025-01-01 09:00:00','2025-01-01 00:00:00');

CREATE TABLE `sku` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `spu_id` int unsigned NOT NULL DEFAULT 0 COMMENT '产品ID',
  `show` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '商品列表是否显示',
  `market_price` int unsigned NOT NULL DEFAULT 0 COMMENT '前台划线价(分)',
  `cost_price` int unsigned NOT NULL DEFAULT 0 COMMENT '成本单价(分)',
  `sale_price` int unsigned NOT NULL DEFAULT 0 COMMENT '销售单价(分)',
  `sold_num` int unsigned NOT NULL DEFAULT 0 COMMENT '已售出数量',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品信息';

CREATE TABLE `trade_order` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '用户id',
  `order_number` int unsigned NOT NULL DEFAULT 0 COMMENT '统一订单号',
  `payment_number` int unsigned NOT NULL DEFAULT 0 COMMENT '支付单唯一编码',
  `order_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `source_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '来源渠道',
  PRIMARY KEY (`id`),
  KEY `idx_tradeorder_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易订单';

CREATE TABLE `trade_order_item` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `order_id` int unsigned NOT NULL DEFAULT 0 COMMENT '订单id',
  `spu_id` int unsigned NOT NULL DEFAULT 0 COMMENT '产品ID',
  `spu_name` varchar(32) NOT NULL DEFAULT '' COMMENT '产品名称',
  `supplier` varchar(32) NOT NULL DEFAULT '' COMMENT '供应商',
  `supplier_param` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商自定义参数',
  `sku_id` int unsigned NOT NULL DEFAULT 0 COMMENT '商品id',
  `sku_count` int unsigned NOT NULL DEFAULT 0 COMMENT '商品数量',
  `market_price` int unsigned NOT NULL DEFAULT 0 COMMENT '前台划线价(分)',
  `cost_price` int unsigned NOT NULL DEFAULT 0 COMMENT '成本单价(分)',
  `sale_price` int unsigned NOT NULL DEFAULT 0 COMMENT '销售单价(分)',
  `discount_price` int unsigned NOT NULL DEFAULT 0 COMMENT '优惠金额(分)',
  `pay_price` int unsigned NOT NULL DEFAULT 0 COMMENT '实际支付金额(分)',
  `service_charge` int unsigned NOT NULL DEFAULT 0 COMMENT '服务费(分)',
  `pay_score` int unsigned NOT NULL DEFAULT 0 COMMENT '支付积分(会员积分)',
  PRIMARY KEY (`id`),
  KEY `idx_tradeorderitem_orderid` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单条目';