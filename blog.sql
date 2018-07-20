
-- 创建数据库blog

DROP TABLE IF EXISTS `t_logs`;

CREATE TABLE `t_logs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `action` varchar(100) DEFAULT NULL COMMENT '动作',
  `data` varchar(2000) DEFAULT NULL COMMENT '数据',
  `author_id` int(10) DEFAULT NULL COMMENT '用户',
  `ip` varchar(20) DEFAULT NULL COMMENT 'IP',
  `created` int(10) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志记录表';

DROP TABLE IF EXISTS `t_attach`;

CREATE TABLE `t_attach` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `fname` varchar(100) NOT NULL DEFAULT '' COMMENT '文件名',
  `ftype` varchar(50) DEFAULT '' COMMENT '文件类型',
  `fkey` varchar(100) NOT NULL DEFAULT '' COMMENT '文件路径',
  `author_id` int(10) DEFAULT NULL COMMENT '上传用户',
  `created` int(10) DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件记录表';

DROP TABLE IF EXISTS `t_comments`;

CREATE TABLE `t_comments` (
  `coid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '评论ID自增',
  `cid` int(10) unsigned DEFAULT '0' COMMENT '文章ID,关联content表主键',
  `created` int(10) DEFAULT NULL COMMENT '评论时间',
  `author` varchar(200) DEFAULT NULL COMMENT '评论者',
  `author_id` int(10) unsigned DEFAULT '0' COMMENT '评论者ID',
  `owner_id` int(10) unsigned DEFAULT '0' COMMENT '文章作者ID',
  `mail` varchar(200) DEFAULT NULL COMMENT '评论者邮件',
  `url` varchar(200) DEFAULT NULL COMMENT '评论者网址',
  `ip` varchar(64) DEFAULT NULL  COMMENT '评论者IP',
  `agent` varchar(200) DEFAULT NULL  COMMENT '评论者客户端',
  `content` text COMMENT '评论内容',
  `type` varchar(16) DEFAULT 'comment' COMMENT '评论类型',
  `status` varchar(16) DEFAULT 'approved' COMMENT '评论状态',
  `parent` int(10) unsigned DEFAULT '0' COMMENT '父级评论',
  PRIMARY KEY (`coid`),
  KEY `cid` (`cid`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

DROP TABLE IF EXISTS `t_contents`;

CREATE TABLE `t_contents` (
  `cid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `title` varchar(200) DEFAULT NULL COMMENT '文章主题',
  `slug` varchar(200) DEFAULT NULL COMMENT '文章缩略名,可用于URL访问,暂定为空',
  `created` int(10) DEFAULT NULL COMMENT '创建时间',
  `modified` int(10) DEFAULT NULL COMMENT '更新时间',
  `content` text COMMENT '内容文字',
  `author_id` int(10) unsigned DEFAULT '0' COMMENT '作者ID',
  `type` varchar(16) DEFAULT 'post' COMMENT '文章类别',
  `status` varchar(16) DEFAULT 'publish' COMMENT '文章状态',
  `tags` varchar(200) DEFAULT NULL COMMENT '文章标签列表',
  `categories` varchar(200) DEFAULT NULL COMMENT '文章分类列表',
  `hits` int(10) unsigned DEFAULT '0' COMMENT '阅读次数',
  `comments_num` int(10) unsigned DEFAULT '0' COMMENT '内容所属评论数',
  `allow_comment` tinyint(1) DEFAULT '1' COMMENT '是否允许评论',
  `allow_ping` tinyint(1) DEFAULT '1' COMMENT '是否允许ping',
  `allow_feed` tinyint(1) DEFAULT '1' COMMENT '是否允许出现在聚合中',
  PRIMARY KEY (`cid`),
  UNIQUE KEY `slug` (`slug`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章表';

LOCK TABLES `t_contents` WRITE;

INSERT INTO `t_contents` (`cid`, `title`, `slug`, `created`, `modified`, `content`, `author_id`, `type`, `status`, `tags`, `categories`, `hits`, `comments_num`, `allow_comment`, `allow_ping`, `allow_feed`)
VALUES
	(1,'about my blog','about','1531095835','1531095835','### Hello World\r\n\r\nabout me\r\n\r\n### ...\r\n\r\n...',1,'page','publish',NULL,NULL,0,0,1,1,1),
	(2,'Hello My Blog',NULL,'1531095835','1531095835','## Hello  World.\r\n\r\n> ...\r\n\r\n----------\r\n\r\n\r\n<!--more-->\r\n\r\n```java\r\npublic static void main(String[] args){\r\n    System.out.println(\"Hello 13 Blog.\");\r\n}\r\n```',1,'post','publish','','default',10,0,1,1,1);

UNLOCK TABLES;

DROP TABLE IF EXISTS `t_metas`;

CREATE TABLE `t_metas` (
  `mid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `slug` varchar(200) DEFAULT NULL COMMENT '缩略名',
  `type` varchar(32) NOT NULL DEFAULT '' COMMENT '类型:标签/类别/链接等',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `sort` int(10) unsigned DEFAULT '0' COMMENT '排序',
  `parent` int(10) unsigned DEFAULT '0' ,
  PRIMARY KEY (`mid`),
  KEY `slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签/类别/链接表';

LOCK TABLES `t_metas` WRITE;

INSERT INTO `t_metas` (`mid`, `name`, `slug`, `type`, `description`, `sort`, `parent`)
VALUES
	(1,'default',NULL,'category',NULL,0,0),
	(6,'my github','https://github.com/wustwxl','link',NULL,0,0);

UNLOCK TABLES;

DROP TABLE IF EXISTS `t_options`;

CREATE TABLE `t_options` (
  `name` varchar(32) NOT NULL DEFAULT '',
  `value` varchar(1000) DEFAULT '',
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置信息表';

LOCK TABLES `t_options` WRITE;

INSERT INTO `t_options` (`name`, `value`, `description`)
VALUES
	('site_title','My Blog',''),
	('social_weibo','',NULL),
	('social_zhihu','',NULL),
	('social_github','',NULL),
	('social_twitter','',NULL),
	('site_theme','default',NULL),
	('site_keywords','13 Blog',NULL),
	('site_description','13 Blog',NULL),
	('site_record','','备案号');

UNLOCK TABLES;

DROP TABLE IF EXISTS `t_relationships`;

CREATE TABLE `t_relationships` (
  `cid` int(10) unsigned NOT NULL,
  `mid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`cid`,`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关系表:文章表与标签/类别/链接表关系';

LOCK TABLES `t_relationships` WRITE;

INSERT INTO `t_relationships` (`cid`, `mid`) VALUES (2,1);

UNLOCK TABLES;

DROP TABLE IF EXISTS `t_users`;

CREATE TABLE `t_users` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `home_url` varchar(200) DEFAULT NULL COMMENT '用户主页URL',
  `screen_name` varchar(32) DEFAULT NULL COMMENT '用户显示的名称',
  `created` int(10) DEFAULT NULL COMMENT '用户注册时间',
  `activated` int(10) DEFAULT NULL COMMENT '最后活动时间',
  `logged` int(10) DEFAULT NULL COMMENT '上次登录最后活跃时间',
  `group_name` varchar(16) DEFAULT 'visitor' COMMENT '用户组',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `name` (`username`),
  UNIQUE KEY `mail` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

INSERT INTO `t_users` (`uid`, `username`, `password`, `email`, `home_url`, `screen_name`, `created`, `activated`, `logged`, `group_name`)
VALUES (1, 'admin', 'a66abb5684c45962d887564f08346e8d', '1175141062@qq.com', NULL, 'admin', '1531095835', '1531095835', '1531095835', 'visitor');

