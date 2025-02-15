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

ALTER TABLE proof_entity ADD CONSTRAINT FK_PROOF_ON_USER FOREIGN KEY (user_id)
    REFERENCES user_entity (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

-- changeset sasha:3
DROP TABLE IF EXISTS kudos_entity CASCADE;

CREATE TABLE kudos_entity
(
    kudos_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id     BIGINT NOT NULL,
    proof_id    BIGINT NOT NULL,
    follower_id BIGINT,
    create_data TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_kudosentity PRIMARY KEY (kudos_id)
);

ALTER TABLE kudos_entity
    ADD CONSTRAINT FK_KUDOSENTITY_ON_OWNER FOREIGN KEY (user_id) REFERENCES user_entity (user_id);

ALTER TABLE kudos_entity
    ADD CONSTRAINT FK_KUDOSENTITY_ON_PROOF FOREIGN KEY (proof_id) REFERENCES proof_entity (proof_id);

-- changeset kate:5
ALTER TABLE proof_entity DROP  CONSTRAINT IF EXISTS FK_PROOF_ON_USER;
ALTER TABLE kudos_entity DROP CONSTRAINT IF EXISTS FK_KUDOSENTITY_ON_OWNER;
ALTER TABLE kudos_entity DROP CONSTRAINT IF EXISTS FK_KUDOSENTITY_ON_PROOF;

-- changeset kate:6

ALTER TABLE kudos_entity ADD COLUMN IF NOT EXISTS count_kudos INTEGER NOT NULL DEFAULT 0;
ALTER TABLE kudos_entity ADD COLUMN IF NOT EXISTS update_data TIMESTAMP WITHOUT TIME ZONE;

DROP TABLE IF EXISTS sponsor_entity CASCADE;
CREATE TABLE sponsor_entity
(
    sponsor_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    full_name  VARCHAR(255),
    email      VARCHAR(255),
    password   VARCHAR(255),
    avatar     VARCHAR(255),
    company    VARCHAR(255),
    unused_kudos INTEGER DEFAULT 100,
    CONSTRAINT pk_sponsorentity PRIMARY KEY (sponsor_id)
);

CREATE TABLE sponsor_entity_authorities
(
    sponsor_entity_sponsor_id BIGINT NOT NULL,
    sponsor_entity_authorities_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    authorities  VARCHAR(255),
    CONSTRAINT pk_sponsorentityauthorities PRIMARY KEY (sponsor_entity_authorities_id)
);

ALTER TABLE sponsor_entity_authorities
    ADD CONSTRAINT fk_sponsorlpos_on_pk_sponsor_entity_authorities FOREIGN KEY (sponsor_entity_authorities_id) REFERENCES sponsor_entity_authorities (sponsor_entity_authorities_id);

-- changeset sasha:7
DROP TABLE IF EXISTS kudos_entity CASCADE;

CREATE TABLE kudos_entity
(
    kudos_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    sponsor_id  BIGINT NOT NULL,
    proof_id    BIGINT NOT NULL,
    follower_id BIGINT,
    count_kudos INTEGER,
    update_data TIMESTAMP WITHOUT TIME ZONE,
    create_data TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_kudosentity PRIMARY KEY (kudos_id)
);

-- changeset kate:8

ALTER TABLE sponsor_entity ADD COLUMN IF NOT EXISTS activation_code VARCHAR(255) NULL;
ALTER TABLE sponsor_entity ADD COLUMN IF NOT EXISTS expiry_date TIMESTAMP WITHOUT TIME ZONE;

-- changeset serhii:9
CREATE TABLE delayed_delete_entity
(
    id                        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    entity_id                  BIGINT,
    user_deleting_process_uuid UUID,
    delete_date               TIMESTAMP WITHOUT TIME ZONE,
    deleting_entity_type      INTEGER,
    CONSTRAINT pk_delayeddeleteentity PRIMARY KEY (id)
);

ALTER TABLE sponsor_entity ADD COLUMN IF NOT EXISTS status VARCHAR(255) DEFAULT 'ACTIVE';

ALTER TABLE kudos_entity ALTER COLUMN sponsor_id DROP NOT NULL;

-- changeset serhii:12
ALTER TABLE delayed_delete_entity ALTER COLUMN deleting_entity_type TYPE VARCHAR(255);

-- changeset kate:13
DROP TABLE IF EXISTS proof_skill CASCADE;
DROP TABLE IF EXISTS skill_entity CASCADE;

CREATE TABLE proof_skill
(
    proof_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    CONSTRAINT pk_proof_skill PRIMARY KEY (proof_id, skill_id)
);

CREATE TABLE skill_entity
(
    skill_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    skill    VARCHAR(255) NULL,
    CONSTRAINT pk_skillentity PRIMARY KEY (skill_id)
);

-- changeset kate:14
ALTER TABLE skill_entity ADD COLUMN IF NOT EXISTS category VARCHAR(255) NULL;

-- changeset sasha:17
DROP TABLE IF EXISTS skill_entity_id_proofs CASCADE;
DROP TABLE IF EXISTS talent_skill CASCADE;

CREATE TABLE skill_entity_id_proofs
(
    skill_entity_skill_id BIGINT NOT NULL,
    skill_entity_id_proofs_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    id_proofs BIGINT,
    CONSTRAINT pk_skillentityidproofs PRIMARY KEY (skill_entity_id_proofs_id)
);

ALTER TABLE skill_entity_id_proofs
    ADD CONSTRAINT fk_skill_on_pk_skill_entity_id_proofs FOREIGN KEY (skill_entity_id_proofs_id) REFERENCES skill_entity_id_proofs (skill_entity_id_proofs_id);

CREATE TABLE talent_skill
(
    skill_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    CONSTRAINT pk_talent_skill PRIMARY KEY (skill_id, user_id)
);

ALTER TABLE talent_skill
    ADD CONSTRAINT fk_talski_on_skill_entity FOREIGN KEY (skill_id) REFERENCES skill_entity (skill_id);

ALTER TABLE talent_skill
    ADD CONSTRAINT fk_talski_on_user_entity FOREIGN KEY (user_id) REFERENCES user_entity (user_id);

-- changeset sasha:18
DROP TABLE IF EXISTS skill_entity_id_proofs CASCADE;