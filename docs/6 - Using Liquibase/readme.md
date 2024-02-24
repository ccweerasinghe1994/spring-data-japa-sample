## 38 - Introduction

![img.png](img.png)

there are main two popular ways to database management tools:

`Flyway`: Flyway is an open-source database migration tool. It strongly favors simplicity and convention over configuration. It is based around just 6 basic commands: Migrate, Clean, Info, Validate, Baseline and Repair. Migrations can be written in SQL (database-specific syntax (such as PL/SQL, T-SQL, ...) is supported) or Java (for advanced data transformations or dealing with LOBs). It has a Command-line tool, a Java API, Maven plugin, Gradle plugin, SBT plugin, Ant tasks and a few more. It is available on the Maven Central Repository and can be easily integrated with Spring applications using Spring Boot starters and other Spring-centric features.

`Liquibase`: Liquibase is an open-source database-independent library for tracking, managing and applying database schema changes. It was started in 2006 to allow easier tracking of database changes, especially in an agile software development environment. It is a tool for managing database changes, and can be used as an alternative to manually writing SQL scripts for database changes. It supports XML, YAML and SQL formats for writing the changes. It has a Command-line tool, a Java API, a Spring Boot starter, a Maven plugin, an Ant task, a Grails plugin, a Play plugin, a Dropwizard plugin, a Clojure wrapper and a few more. It is available on the Maven Central Repository and can be easily integrated with Spring applications using Spring Boot starters and other Spring-centric features.

## 39 - OverviewOfLiquidbase

### what is migration?

![img_1.png](img_1.png)

### why use a migration tool?

![img_2.png](img_2.png)
![img_3.png](img_3.png)

Liquibase and Flyway

![img_4.png](img_4.png)

Springboot support both of them

![img_5.png](img_5.png)
![img_6.png](img_6.png)

which one to choose?

![img_7.png](img_7.png)

### Liquibase 

![img_8.png](img_8.png)
### Liquibase Terminology
![img_9.png](img_9.png)

### Liquibase Best Practices
![img_10.png](img_10.png)

### Running Liquibase
![img_11.png](img_11.png)
### Next Steps
![img_12.png](img_12.png)
## 40 - Liquibase Maven Plugin
## 41 - Generate Changeset from Database
## 42 - Organizing Change Logs
## 43 - Spring Boot Configuration
## 44 - Initializing Data with Spring
## 45 - Alter Table with Liquibase
