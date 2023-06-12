-- liquibase formatted sql
-- changeset kate:1
DROP TABLE IF EXISTS talent CASCADE;
DROP TABLE IF EXISTS position CASCADE;
DROP TABLE IF EXISTS talent_authorities CASCADE;
DROP TABLE IF EXISTS talent_position CASCADE;

CREATE TABLE talent
(
    talent_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    full_name  VARCHAR(255)                            NOT NULL,
    email      VARCHAR(255)                            NOT NULL,
    password   VARCHAR(255)                            NOT NULL,
    birthday   date,
    avatar     VARCHAR(255),
    education  VARCHAR(255),
    experience VARCHAR(255),
    CONSTRAINT pk_userentity PRIMARY KEY (talent_id)
);

CREATE TABLE position
(
    position_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    position    VARCHAR(255),
    CONSTRAINT pk_positionentity PRIMARY KEY (position_id)
);

CREATE TABLE talent_position
(
    position_id BIGINT NOT NULL,
    talent_id   BIGINT NOT NULL
);

CREATE TABLE talent_authorities
(
    talent_talent_id      BIGINT                                  NOT NULL,
    talent_authorities_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    authorities           VARCHAR(255),
    CONSTRAINT pk_userentityauthorities PRIMARY KEY (talent_authorities_id)
);

ALTER TABLE talent_authorities
    ADD CONSTRAINT fk_userlpos_on_pk_user_entity_authorities FOREIGN KEY (talent_authorities_id) REFERENCES talent_authorities (talent_authorities_id);
-- changeset kate:2
DROP TABLE IF EXISTS proof CASCADE;

CREATE TABLE proof
(
    proof_id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title             VARCHAR(255),
    description       VARCHAR(1000),
    link              VARCHAR(255),
    status            VARCHAR(255),
    date_created      TIMESTAMP WITH TIME ZONE,
    date_last_updated TIMESTAMP WITHOUT TIME ZONE,
    talent_id         BIGINT                                  NOT NULL,
    CONSTRAINT pk_proof PRIMARY KEY (proof_id)
);

ALTER TABLE proof
    ADD CONSTRAINT FK_PROOF_ON_USER FOREIGN KEY (talent_id)
        REFERENCES talent (talent_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

-- changeset sasha:3
DROP TABLE IF EXISTS kudos CASCADE;

CREATE TABLE kudos
(
    kudos_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    talent_id   BIGINT                                  NOT NULL,
    proof_id    BIGINT                                  NOT NULL,
    follower_id BIGINT,
    create_data TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_kudosentity PRIMARY KEY (kudos_id)
);

ALTER TABLE kudos
    ADD CONSTRAINT FK_KUDOSENTITY_ON_OWNER FOREIGN KEY (talent_id) REFERENCES talent (talent_id);

ALTER TABLE kudos
    ADD CONSTRAINT FK_KUDOSENTITY_ON_PROOF FOREIGN KEY (proof_id) REFERENCES proof (proof_id);

-- changeset kate:5
ALTER TABLE proof
    DROP CONSTRAINT IF EXISTS FK_PROOF_ON_USER;
ALTER TABLE kudos
    DROP CONSTRAINT IF EXISTS FK_KUDOSENTITY_ON_OWNER;
ALTER TABLE kudos
    DROP CONSTRAINT IF EXISTS FK_KUDOSENTITY_ON_PROOF;

-- changeset kate:6

ALTER TABLE kudos
    ADD COLUMN IF NOT EXISTS count_kudos INTEGER NOT NULL DEFAULT 0;
ALTER TABLE kudos
    ADD COLUMN IF NOT EXISTS update_data TIMESTAMP WITHOUT TIME ZONE;

DROP TABLE IF EXISTS sponsor CASCADE;
CREATE TABLE sponsor
(
    sponsor_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    full_name    VARCHAR(255),
    email        VARCHAR(255),
    password     VARCHAR(255),
    avatar       VARCHAR(255),
    company      VARCHAR(255),
    unused_kudos INTEGER DEFAULT 100,
    CONSTRAINT pk_sponsorentity PRIMARY KEY (sponsor_id)
);

CREATE TABLE sponsor_authorities
(
    sponsor_sponsor_id     BIGINT                                  NOT NULL,
    sponsor_authorities_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    authorities            VARCHAR(255),
    CONSTRAINT pk_sponsorentityauthorities PRIMARY KEY (sponsor_authorities_id)
);

ALTER TABLE sponsor_authorities
    ADD CONSTRAINT fk_sponsorlpos_on_pk_sponsor_entity_authorities FOREIGN KEY (sponsor_authorities_id) REFERENCES sponsor_authorities (sponsor_authorities_id);

-- changeset sasha:7
DROP TABLE IF EXISTS kudos CASCADE;

CREATE TABLE kudos
(
    kudos_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    sponsor_id  BIGINT                                  NOT NULL,
    proof_id    BIGINT                                  NOT NULL,
    follower_id BIGINT,
    count_kudos INTEGER,
    update_data TIMESTAMP WITHOUT TIME ZONE,
    create_data TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_kudosentity PRIMARY KEY (kudos_id)
);

-- changeset kate:8

ALTER TABLE sponsor
    ADD COLUMN IF NOT EXISTS activation_code VARCHAR(255) NULL;
ALTER TABLE sponsor
    ADD COLUMN IF NOT EXISTS expiry_date TIMESTAMP WITHOUT TIME ZONE;

-- changeset serhii:9
CREATE TABLE delayed_delete
(
    id                         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    entity_id                  BIGINT,
    user_deleting_process_uuid UUID,
    delete_date                TIMESTAMP WITHOUT TIME ZONE,
    deleting_entity_type       INTEGER,
    CONSTRAINT pk_delayeddeleteentity PRIMARY KEY (id)
);

ALTER TABLE sponsor
    ADD COLUMN IF NOT EXISTS status VARCHAR(255) DEFAULT 'ACTIVE';

ALTER TABLE kudos
    ALTER COLUMN sponsor_id DROP NOT NULL;

-- changeset serhii:12
ALTER TABLE delayed_delete
    ALTER COLUMN deleting_entity_type TYPE VARCHAR(255);

-- changeset kate:13
DROP TABLE IF EXISTS proof_skill CASCADE;
DROP TABLE IF EXISTS skill CASCADE;

CREATE TABLE IF NOT EXISTS proof_skill
(
    proof_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    CONSTRAINT pk_proof_skill PRIMARY KEY (proof_id, skill_id)
);

CREATE TABLE skill
(
    skill_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    skill    VARCHAR(255)                            NULL,
    CONSTRAINT pk_skillentity PRIMARY KEY (skill_id)
);

-- changeset kate:14
ALTER TABLE skill
    ADD COLUMN IF NOT EXISTS category VARCHAR(255) NULL;

-- changeset sasha:17
DROP TABLE IF EXISTS skill_id_proofs CASCADE;
DROP TABLE IF EXISTS talent_skill CASCADE;

CREATE TABLE skill_id_proofs
(
    skill_skill_id     BIGINT                                  NOT NULL,
    skill_id_proofs_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    id_proofs          BIGINT,
    CONSTRAINT pk_skillentityidproofs PRIMARY KEY (skill_id_proofs_id)
);

ALTER TABLE skill_id_proofs
    ADD CONSTRAINT fk_skill_on_pk_skill_entity_id_proofs FOREIGN KEY (skill_id_proofs_id) REFERENCES skill_id_proofs (skill_id_proofs_id);

CREATE TABLE IF NOT EXISTS talent_skill
(
    skill_id  BIGINT NOT NULL,
    talent_id BIGINT NOT NULL,
    CONSTRAINT pk_talent_skill PRIMARY KEY (skill_id, talent_id)
);

ALTER TABLE talent_skill
    ADD CONSTRAINT fk_talski_on_skill_entity FOREIGN KEY (skill_id) REFERENCES skill (skill_id);

ALTER TABLE talent_skill
    ADD CONSTRAINT fk_talski_on_user_entity FOREIGN KEY (talent_id) REFERENCES talent (talent_id);

-- changeset sasha:18
DROP TABLE IF EXISTS skill_id_proofs CASCADE;