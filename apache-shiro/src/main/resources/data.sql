-- 权限表

INSERT INTO permission VALUES (1,'add','');
INSERT INTO permission VALUES (2,'delete','');
INSERT INTO permission VALUES (3,'modify','');
INSERT INTO permission VALUES (4,'query','');

-- 用户表

INSERT INTO user VALUES (1,'admin','123');
INSERT INTO user VALUES (2,'user','123');

-- 角色表

INSERT INTO role VALUES (1,'admin');
INSERT INTO role VALUES (2,'user');

-- 权限角色表

INSERT INTO role_permission VALUES (1,1);
INSERT INTO role_permission VALUES (1,2);
INSERT INTO role_permission VALUES (1,3);
INSERT INTO role_permission VALUES (1,4);
INSERT INTO role_permission VALUES (2,1);
INSERT INTO role_permission VALUES (2,4);

-- 用户角色表

INSERT INTO user_role VALUES (1,1);
INSERT INTO user_role VALUES (2,2);