-- liquibase formatted sql
-- changeset kate:1
DROP TABLE IF EXISTS user_entity CASCADE;
DROP TABLE IF EXISTS position_entity CASCADE;
DROP TABLE IF EXISTS user_entity_authorities CASCADE;
DROP TABLE IF EXISTS user_position CASCADE;

CREATE TABLE user_entity
(
    user_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    full_name  VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    birthday   date,
    avatar     VARCHAR(255),
    education  VARCHAR(255),
    experience VARCHAR(255),
    CONSTRAINT pk_userentity PRIMARY KEY (user_id)
);

CREATE TABLE position_entity
(
    position_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    position    VARCHAR(255),
    CONSTRAINT pk_positionentity PRIMARY KEY (position_id)
);

CREATE TABLE user_position
(
    position_id BIGINT NOT NULL,
    user_id     BIGINT NOT NULL
);

CREATE TABLE user_entity_authorities
(
    user_entity_user_id BIGINT NOT NULL,
    user_entity_authorities_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    authorities  VARCHAR(255),
    CONSTRAINT pk_userentityauthorities PRIMARY KEY (user_entity_authorities_id)
);

ALTER TABLE user_entity_authorities
    ADD CONSTRAINT fk_userlpos_on_pk_user_entity_authorities FOREIGN KEY (user_entity_authorities_id) REFERENCES user_entity_authorities (user_entity_authorities_id);
-- changeset kate:2
DROP TABLE IF EXISTS proof_entity CASCADE;

CREATE TABLE proof_entity
(
    proof_id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title        VARCHAR(255),
    description  VARCHAR(1000),
    link         VARCHAR(255),
    status       VARCHAR(255),
    date_created TIMESTAMP WITH TIME ZONE,
    date_last_updated TIMESTAMP WITHOUT TIME ZONE,
    user_id      BIGINT                                  NOT NULL,
    CONSTRAINT pk_proof PRIMARY KEY (proof_id)
);

ALTER TABLE proof_entity
    ADD CONSTRAINT FK_PROOF_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (user_id);