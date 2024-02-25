## 46 - Introduction
![img.png](img.png)

## 47 - Overview of Flyway
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)
![img_6.png](img_6.png)
![img_7.png](img_7.png)
![img_8.png](img_8.png)
![img_9.png](img_9.png)
![img_10.png](img_10.png)
![img_11.png](img_11.png)
![img_12.png](img_12.png)

## 48 - Spring Boot Configuration
make sure the profile is local
so, it will override the default profile

![img_13.png](img_13.png)

add the flyway dependency
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
<groupId>org.flywaydb</groupId>
<artifactId>flyway-mysql</artifactId>
</dependency>
```

![img_18.png](img_18.png)

let's add the flyway configuration in the application.local.properties
```properties
spring.datasource.username=bookuser
spring.datasource.password=password
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/bookdb?useUnicode=true&characterEncoding=UTF-8&ServerTimezone=UTC
spring.jpa.hibernate.ddl-auto=validate
spring.sql.init.mode=always

spring.flyway.user=bookadmin
spring.flyway.password=password
```
let's move the sql file to the resources/db/migration folder
```sql
drop table if exists book;
drop table if exists book_seq;

create table book
(
    id        bigint not null,
    isbn      varchar(255),
    publisher varchar(255),
    title     varchar(255),
    primary key (id)
) engine = InnoDB;

create table book_seq
(
    next_val bigint
) engine = InnoDB;

insert into book_seq
values (1);
```

![img_14.png](img_14.png)
![img_15.png](img_15.png)
![img_16.png](img_16.png)
![img_17.png](img_17.png)
## 49 - Alter Table with Flyway
let's add a author class
```java
package chamara.springdatajpasample.sdjpademo.domain;

import jakarta.persistence.*;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    public Author() {
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
```

```sql
drop table if exists book;
drop table if exists book_seq;

create table book (
                      id bigint not null,
                      isbn varchar(255),
                      publisher varchar(255),
                      title varchar(255),
                      primary key (id)
) engine=InnoDB;

create table book_seq (
                          next_val bigint
) engine=InnoDB;

insert into book_seq values ( 1 );
```
```sql
drop table if exists author;
drop table if exists author_seq;

create table author
(
    id         bigint not null,
    first_name varchar(255),
    last_name varchar(255),
    primary key (id)
) engine = InnoDB;

create table author_seq (
                            next_val bigint
) engine=InnoDB;

insert into author_seq values ( 1 );
```
![img_19.png](img_19.png)

## 50 - Clean and Rebuild with Flyway
