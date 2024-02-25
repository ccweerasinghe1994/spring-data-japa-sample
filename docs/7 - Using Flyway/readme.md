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


let's alter the book table to add an author_id column
create a new migration file `V3__alter_book_table.sql`

```sql
alter table book add column author_id bigint;
```
```java
package chamara.springdatajpasample.sdjpademo.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Book {
    private String isbn;
    private String title;
    private String publisher;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Long authorId;

    public Book() {

    }
    public Book(String isbn, String title, String publisher, Long authorId) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.authorId = authorId;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

```
![img_20.png](img_20.png)

## 50 - Clean and Rebuild with Flyway

![img_22.png](img_22.png)
![img_23.png](img_23.png)

let's add a configuration to clean the database and rebuild it
```java
package chamara.springdatajpasample.sdjpademo.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("clean")
public class DbClean {

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}
```

let's add a new profile in the application-clean.properties
```properties
spring.flyway.clean-disabled=false
```
![img_21.png](img_21.png)
