-- liquibase formatted sql

-- changeset chamara:1708753296657-1
CREATE TABLE book (id BIGINT NOT NULL, isbn VARCHAR(255) NULL, publisher VARCHAR(255) NULL, title VARCHAR(255) NULL, CONSTRAINT PK_BOOK PRIMARY KEY (id));

-- changeset chamara:1708753296657-2
CREATE TABLE book_seq (next_val BIGINT NULL);

