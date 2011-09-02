
CREATE DATABASE IF NOT EXISTS `@@name`;

USE `@@name`;

DROP TABLE IF EXISTS `sys_async_response`;

CREATE TABLE `sys_async_response` (
  `objid` varchar(50) NOT NULL,
  `requestId` varchar(50) default NULL,
  `data` blob,
  `expiryDate` datetime default NULL,
  `idx` int(11) default NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sys_ds`;

CREATE TABLE `sys_ds` (
  `name` varchar(50) NOT NULL,
  `host` varchar(20) default NULL,
  `dbname` varchar(50) default NULL,
  `user` varchar(50) default NULL,
  `pwd` varchar(50) default NULL,
  `scheme` varchar(10) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sys_rms_cluster`;

CREATE TABLE `sys_rms_cluster` (
  `host` varchar(50) NOT NULL,
  PRIMARY KEY  (`host`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `sys_schema`;

CREATE TABLE `sys_schema` (
  `name` varchar(50) NOT NULL,
  `content` text,
  `category` varchar(255) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sys_script`;

CREATE TABLE `sys_script` (
  `name` varchar(50) NOT NULL,
  `content` text,
  `category` varchar(255) default NULL,
  `notes` text,
  PRIMARY KEY  (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sys_session`;

CREATE TABLE `sys_session` (
  `sessionid` varchar(50) NOT NULL,
  `username` varchar(50) default NULL,
  `userid` varchar(50) default NULL,
  `info` mediumtext NOT NULL,
  `dtaccessed` datetime default NULL,
  `dtexpiry` datetime default NULL,
  PRIMARY KEY  (`sessionid`),
  KEY `username` (`username`),
  KEY `userid` (`userid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sys_sql`;

CREATE TABLE `sys_sql` (
  `name` varchar(50) NOT NULL,
  `content` text,
  `category` varchar(255) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sys_var`;

CREATE TABLE `sys_var` (
  `name` varchar(50) NOT NULL,
  `value` text,
  `description` varchar(255) default NULL,
  `type` varchar(15) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


