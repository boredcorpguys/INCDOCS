-- Database: INCDOCS_DB

CREATE DATABASE "INCDOCS_DB"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_India.1252'
    LC_CTYPE = 'English_India.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;


-- SCHEMA: INCDOCS

CREATE SCHEMA "INCDOCS"
    AUTHORIZATION postgres;


-- Table: "INCDOCS"."ACTIONS"

CREATE TABLE "INCDOCS"."ACTIONS"
(
    "ID" integer NOT NULL DEFAULT nextval('"INCDOCS"."ACTIONS_ID_seq"'::regclass),
    "NAME" character varying(30) COLLATE pg_catalog."default" NOT NULL,
    "DESCRIPTION" character varying(100) COLLATE pg_catalog."default",
    "IS_ACTIVE" boolean NOT NULL DEFAULT true,
    CONSTRAINT "ACTIONS_pkey" PRIMARY KEY ("ID")
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "INCDOCS"."ACTIONS"
    OWNER to postgres;


-- Table: "INCDOCS"."ENTITIES"

CREATE TABLE "INCDOCS"."ENTITIES"
(
    "ID" character varying(20) COLLATE pg_catalog."default" NOT NULL DEFAULT uuid_generate_v1(),
    "PARENT_ID" character varying(20) COLLATE pg_catalog."default",
    "NAME" character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "PAN" character(10) COLLATE pg_catalog."default",
    "IS_CLIENT" boolean NOT NULL DEFAULT false,
    "IS_ACTIVE" boolean NOT NULL DEFAULT true,
    CONSTRAINT "ENTITIES_PK" PRIMARY KEY ("ID")
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "INCDOCS"."ENTITIES"
    OWNER to postgres;


-- Table: "INCDOCS"."ROLES"

CREATE TABLE "INCDOCS"."ROLES"
(
    "ID" integer NOT NULL DEFAULT nextval('"INCDOCS"."ROLES_ID_seq"'::regclass),
    "NAME" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    "DESCRIPTION" character varying(100) COLLATE pg_catalog."default",
    "IS_CLIENT" boolean NOT NULL DEFAULT false,
    "IS_ACTIVE" boolean NOT NULL DEFAULT true,
    CONSTRAINT "ROLES_PK" PRIMARY KEY ("ID")
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "INCDOCS"."ROLES"
    OWNER to postgres;


-- Table: "INCDOCS"."USERS"

CREATE TABLE "INCDOCS"."USERS"
(
    "NAME" character varying(255) COLLATE pg_catalog."default" NOT NULL,
    "INCDOCS_ID" character varying(20) COLLATE pg_catalog."default" NOT NULL DEFAULT uuid_generate_v1(),
    "EMP_ID" character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "MANAGER_ID" character varying(100) COLLATE pg_catalog."default",
    "ROLE_ID" integer NOT NULL,
    "PASSWORD" character(16) COLLATE pg_catalog."default",
    "EMAIL_ID" character varying(30) COLLATE pg_catalog."default",
    "CONTACT_NUMBER" character(15) COLLATE pg_catalog."default",
    "COMPANY_ID" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    "IS_CLIENT" boolean NOT NULL DEFAULT false,
    "STATUS" character(1) COLLATE pg_catalog."default" NOT NULL DEFAULT 'I'::bpchar,
    CONSTRAINT "USERS_PK" PRIMARY KEY ("INCDOCS_ID"),
    CONSTRAINT users_company FOREIGN KEY ("COMPANY_ID")
        REFERENCES "INCDOCS"."ENTITIES" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT users_role FOREIGN KEY ("ROLE_ID")
        REFERENCES "INCDOCS"."ROLES" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "INCDOCS"."USERS"
    OWNER to postgres;


-- Table: "INCDOCS"."ENTITY_ROLES"

CREATE TABLE "INCDOCS"."ENTITY_ROLES"
(
    "ENTITY_ID" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    "ROLE_ID" integer NOT NULL,
    "DISPLAY_NAME" character varying(30) COLLATE pg_catalog."default",
    "IS_ACTIVE" boolean NOT NULL DEFAULT true,
    CONSTRAINT "ENTITY_ROLES_pkey" PRIMARY KEY ("ENTITY_ID", "ROLE_ID"),
    CONSTRAINT entity_fk FOREIGN KEY ("ENTITY_ID")
        REFERENCES "INCDOCS"."ENTITIES" ("ID") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)

-- Table: "INCDOCS"."ROLE_ACTIONS"

CREATE TABLE "INCDOCS"."ROLE_ACTIONS"
(
    "ROLE_ID" integer NOT NULL,
    "ACTION_ID" integer NOT NULL,
    "IS_ACTIVE" boolean NOT NULL DEFAULT true,
    CONSTRAINT "ROLE_ACTIONS_pkey" PRIMARY KEY ("ROLE_ID", "ACTION_ID"),
    CONSTRAINT action_fk FOREIGN KEY ("ACTION_ID")
        REFERENCES "INCDOCS"."ACTIONS" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT role_fk FOREIGN KEY ("ROLE_ID")
        REFERENCES "INCDOCS"."ROLES" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "INCDOCS"."ROLE_ACTIONS"
    OWNER to postgres;
TABLESPACE pg_default;

-- Table: "INCDOCS"."SERVICE_ROLES"

CREATE TABLE "INCDOCS"."SERVICE_ROLES"
(
    "SERVICE_URI" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    "ROLE_ID" integer NOT NULL,
    CONSTRAINT "SERVICE_ROLES_pkey" PRIMARY KEY ("SERVICE_URI"),
    CONSTRAINT role_fk FOREIGN KEY ("ROLE_ID")
        REFERENCES "INCDOCS"."ROLES" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "INCDOCS"."SERVICE_ROLES"
    OWNER to postgres;
ALTER TABLE "INCDOCS"."ENTITY_ROLES"
    OWNER to postgres;


-- Table: "INCDOCS"."USER_ENTITLEMENTS"

CREATE TABLE "INCDOCS"."USER_ENTITLEMENTS"
(
    "USER_ID" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    "ENTITY_ID" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    "IS_ACTIVE" boolean NOT NULL DEFAULT true,
    CONSTRAINT "USER_ENTITLEMENTS_pkey" PRIMARY KEY ("USER_ID", "ENTITY_ID"),
    CONSTRAINT entity_fk FOREIGN KEY ("ENTITY_ID")
        REFERENCES "INCDOCS"."ENTITIES" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT user_fk FOREIGN KEY ("USER_ID")
        REFERENCES "INCDOCS"."USERS" ("INCDOCS_ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "INCDOCS"."USER_ENTITLEMENTS"
    OWNER to postgres;
COMMENT ON TABLE "INCDOCS"."ENTITY_ROLES"
    IS 'Defines the roles available for an entity';

CREATE SEQUENCE "INCDOCS"."ACTIONS_ID_seq"
    INCREMENT 1
    START 16
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE "INCDOCS"."ACTIONS_ID_seq"
    OWNER TO postgres;

CREATE SEQUENCE "INCDOCS"."ROLES_ID_seq"
    INCREMENT 1
    START 9
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE "INCDOCS"."ROLES_ID_seq"
    OWNER TO postgres;

