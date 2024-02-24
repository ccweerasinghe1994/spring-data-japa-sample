# drop table if exists book;
# drop table if exists book_seq;

create table book
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn      varchar(255),
    publisher varchar(255),
    title     varchar(255)
) engine = InnoDB;

# create table book_seq
# (
#     next_val bigint
# ) engine = InnoDB;
#
# insert into book_seq
# values (1);

