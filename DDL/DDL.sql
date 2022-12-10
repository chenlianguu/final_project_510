create table iit.chen_books
(
    bid         int auto_increment
        primary key,
    author      varchar(100)                         null,
    title       varchar(100)                         null,
    status      tinyint(1) default 0                 null comment '0 available 1 unavailable',
    create_time timestamp  default CURRENT_TIMESTAMP null,
    update_time timestamp                            null on update CURRENT_TIMESTAMP
);

create table iit.chen_orders
(
    id          int auto_increment comment 'id'
        primary key,
    uid         int                                 null comment 'user id',
    bid         int                                 null,
    action      tinyint(1)                          null comment '0 return book 1 borrow book',
    create_time timestamp default CURRENT_TIMESTAMP null,
    update_time timestamp                           null on update CURRENT_TIMESTAMP
);

create table iit.chen_users
(
    id          int auto_increment
        primary key,
    uname       varchar(50)                          not null,
    passwd      varchar(64)                          not null,
    admin       tinyint(1) default 0                 not null,
    create_time timestamp  default CURRENT_TIMESTAMP null,
    update_time timestamp                            null on update CURRENT_TIMESTAMP
);

