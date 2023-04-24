--liquibase formatted sql

--changeset vbandurin7:create_chat_table
CREATE TABLE IF NOT EXISTS chat
(
    id BIGINT PRIMARY KEY
);

--changeset vbandurin7:create_link_table
CREATE TABLE IF NOT EXISTS link
(
    id              BIGSERIAL PRIMARY KEY,
    url             VARCHAR(2048) NOT NULL UNIQUE,
    link_info       jsonb NOT NULL,
    last_checked_at timestamp with time zone,
    updated_at      timestamp with time zone
);

--changeset vbandurin7:create_link_chat_table
CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id  BIGINT REFERENCES chat (id),
    link_url VARCHAR(2048) REFERENCES link (url),
    PRIMARY KEY (chat_id, link_url)
);