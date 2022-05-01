-- liquibase formatted sql

-- changeSet rickln: 1001
CREATE TABLE notification_task
(
    idTask   SERIAL NOT NULL PRIMARY KEY,
    idChat   bigint NOT NULL,
    stamp    timestamp NOT NULL,
    textChat varchar(255) NOT NULL,
    status   BOOLEAN
);
