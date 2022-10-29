-- liquibase formatted sql

-- changeset tuzlukov:10
CREATE TABLE notification_task(
    id    SERIAL,
    text TEXT,
    date_time timestamp,
    chat_id BIGINT
)