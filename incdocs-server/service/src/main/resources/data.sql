insert into INCDOCS.USERS(id,name,pan) values(1,'Rahul','abcd1234a');
insert into INCDOCS.USERS(id,name,pan) values(2,'Vishnu','abcd1234b');
insert into INCDOCS.USERS(id,name,pan) values(3,'Kaustubh','abcd1234c');


insert into INCDOCS.ENTITIES(id,name,parent_id) values(1,'RIL',NULL);
insert into INCDOCS.ENTITIES(id,name,parent_id) values(2,'RCOM',1);
insert into INCDOCS.ENTITIES(id,name,parent_id) values(3,'REnergy',1);
insert into INCDOCS.ENTITIES(id,name,parent_id) values(4,'Infosys',NULL);


insert into INCDOCS.RESOURCE_GROUP(id,entity_id) values(1,1);
insert into INCDOCS.RESOURCE_GROUP(id,entity_id) values(2,4);

insert into INCDOCS.ROLES(id,name,description) values(1,'Admin','Admin managing the relationships');
insert into INCDOCS.ROLES(id,name,description) values(2,'RM','Relationship Manager');
insert into INCDOCS.ROLES(id,name,description) values(3,'ARM','Assistant RM');

insert into INCDOCS.ACTIONS(id,name,description) values(1,'Bulk Upload','Bulk Upload the RM and Company Mappings');
insert into INCDOCS.ACTIONS(id,name,description) values(2,'Add User','Add new Relationship Manager');
insert into INCDOCS.ACTIONS(id,name,description) values(3,'Add Company','Add new Company');
insert into INCDOCS.ACTIONS(id,name,description) values(4,'Add Mapping','Setup a RM:Company Mapping');

insert into INCDOCS.ROLE_ACTIONS(role_id,action_id) values(1,1);
insert into INCDOCS.ROLE_ACTIONS(role_id,action_id) values(1,2);
insert into INCDOCS.ROLE_ACTIONS(role_id,action_id) values(1,3);
insert into INCDOCS.ROLE_ACTIONS(role_id,action_id) values(1,4);

insert into INCDOCS.USER_OPERATIONS(user_id,role_id,rg_id) values(1,1,1);