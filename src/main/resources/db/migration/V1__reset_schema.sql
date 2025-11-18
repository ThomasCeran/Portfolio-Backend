DROP TABLE IF EXISTS project_skill CASCADE;
DROP TABLE IF EXISTS project_images CASCADE;
DROP TABLE IF EXISTS project_tags CASCADE;
DROP TABLE IF EXISTS contact_message CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS skill CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS role CASCADE;

CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE skill (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    level VARCHAR(100),
    icon TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE project (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    summary TEXT,
    status VARCHAR(64) NOT NULL,
    cover_image TEXT,
    repo_url TEXT,
    live_url TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE project_images (
    project_id UUID NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    PRIMARY KEY (project_id, image_url)
);

CREATE TABLE project_tags (
    project_id UUID NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    tag TEXT NOT NULL,
    PRIMARY KEY (project_id, tag)
);

CREATE TABLE project_skill (
    project_id UUID NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    skill_id BIGINT NOT NULL REFERENCES skill (id) ON DELETE CASCADE,
    PRIMARY KEY (project_id, skill_id)
);

CREATE TABLE contact_message (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL REFERENCES role (id),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);
