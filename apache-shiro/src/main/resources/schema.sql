-- 权限表

CREATE TABLE permission (
  pid INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(256) DEFAULT '',
  url VARCHAR(256) DEFAULT '',
  PRIMARY key (pid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- 用户表

CREATE TABLE user (
  uid INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(256) DEFAULT '',
  password VARCHAR(256) DEFAULT '',
  PRIMARY KEY (uid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- 角色表

CREATE TABLE role (
  rid INT(11) NOT NULL AUTO_INCREMENT,
  rname VARCHAR(256) DEFAULT '',
  PRIMARY KEY (rid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- 角色权限关系表

CREATE TABLE role_permission (
  rid INT(11) NOT NULL,
  pid INT(11) NOT NULL
  -- KEY index_rid (rid),
  -- KEY index_pid (pid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- 用户角色关系表

CREATE TABLE user_role (
  uid INT(11) NOT NULL,
  rid INT(11) NOT NULL
  -- KEY index_uid (uid),
  -- KEY index_rid (rid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;