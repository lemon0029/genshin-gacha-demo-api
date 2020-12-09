# 额，还是使用 spring data jpa 自动 DDL 操作吧，下面的 SQL 仅供参考
# 不使用外键...除此之外尽量规范建表

create table if not exists genshin_character_attribute
(
    id   int         not null comment '角色属性ID',
    name varchar(32) not null comment '角色属性名称',
    primary key (id)
) engine = InnoDB
  charset = utf8mb4;

create table if not exists genshin_character
(
    id                     bigint        not null comment '角色ID',
    name                   varchar(32)   not null comment '角色名称',
    avatar                 varchar(1000) not null comment '角色头像',
    rank_v                 int           not null comment '角色星级',
    character_attribute_id int           not null comment '角色所属类型ID',
    primary key (id)
) engine = InnoDB
  charset = utf8mb4;

create table if not exists genshin_weapon
(
    id     bigint        not null comment '武器ID',
    name   varchar(32)   not null comment '武器名称',
    avatar varchar(1000) not null comment '武器头像',
    rank_v int           not null comment '武器星级',
    primary key (id)
) engine = InnoDB
  charset = utf8mb4;

create table if not exists genshin_gacha_pool
(
    id         varchar(100) not null comment 'gacha id',
    name       varchar(32)  not null comment 'gacha name',
    type       int          not null comment 'gacha type',
    begin_time datetime     not null comment 'gacha begin time',
    end_time   datetime     not null comment 'gacha end time',
    primary key (id)
) engine = InnoDB
  charset = utf8mb4;
