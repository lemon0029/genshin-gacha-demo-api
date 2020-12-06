create table if not exists genshin_character
(
    id                     bigint        not null comment '角色ID',
    name                   varchar(32)   not null comment '角色名称',
    avatar                 varchar(1000) not null comment '角色头像',
    ranting                int           not null comment '角色星级',
    character_attribute_id int           not null comment '角色所属类型ID',
    primary key (id)
) engine = InnoDB
  charset = utf8mb4;

create table if not exists genshin_weapon
(
    id      bigint        not null comment '武器ID',
    name    varchar(32)   not null comment '武器名称',
    avatar  varchar(1000) not null comment '武器头像',
    ranting int           not null comment '武器星级',
    primary key (id)
) engine = InnoDB
  charset = utf8mb4;