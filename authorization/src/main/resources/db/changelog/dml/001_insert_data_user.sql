--liquibase formatted sql
-- changeset kate:21
INSERT INTO role (name)
VALUES ('ROLE_TALENT');
INSERT INTO role (name)
VALUES ('ROLE_SPONSOR');
INSERT INTO role (name)
VALUES ('ROLE_ADMIN');