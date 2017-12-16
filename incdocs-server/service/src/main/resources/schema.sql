create table INCDOCS.USERS
(
    id integer not null,
    name varchar(255) not null,
    pan character(10) not null,
    primary key(id)
);
create table INCDOCS.ENTITIES
(
    id integer not null,
    name varchar(255) not null,
    parent_id varchar(255),
    primary key(id)
);
create table INCDOCS.RESOURCE_GROUP
(
    id integer not null,
    entity_id integer references ENTITIES(id),
    primary key(id)
);
create table INCDOCS.ROLES
(
    id integer not null,
    name varchar(20) not null,
    description  varchar(255),
    primary key(id)
);
create table INCDOCS.ACTIONS
(
    id integer not null,
    name varchar(20) not null,
    description  varchar(255),
    primary key(id)
);
create table INCDOCS.ROLE_ACTIONS
(
    role_id integer references ROLES(id),
    action_id integer references ACTIONS(id),
    primary key(role_id,action_id)
);
create table INCDOCS.USER_OPERATIONS
(
    user_id integer references USERS(id),
    role_id integer references ROLES(id),
    rg_id integer references RESOURCE_GROUP(id),
    primary key(user_id,role_id,rg_id)
);