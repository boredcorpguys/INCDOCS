create table INCDOCS.ROLES
(
    id integer not null,
    name varchar(20) not null,
    description  varchar(255),
    primary key(id)
);
create table INCDOCS.USERS
(
    name varchar(255) not null,
    emp_id varchar(100) not null,
    email_id varchar(100) not null,
    role_id integer references ROLES(id),
    password varchar(20) not null,
    primary key(email_id)
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
    user_id varchar(100) references USERS(email_id),
    rg_id integer references RESOURCE_GROUP(id),
    primary key(user_id,rg_id)
);