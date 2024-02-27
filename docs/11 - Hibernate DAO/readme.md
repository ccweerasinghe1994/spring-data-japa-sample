## 82 - Introduction

![img.png](img.png)

## 83 - Introduction to Hibernate

![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)
![img_6.png](img_6.png)
![img_7.png](img_7.png)
![img_8.png](img_8.png)

## 84 - Project Code Review

## 85 - Implement Get Author By Id

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthorDaoImpl implements AuthorDoa {
    private final EntityManagerFactory emf;

    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Author getById(Long id) {
        return getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        return null;
    }

    @Override
    public void deleteAuthorById(Long id) {

    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
```

## 86 - Implement Find Author By Name

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class AuthorDaoImpl implements AuthorDoa {
    private final EntityManagerFactory emf;

    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Author getById(Long id) {
        return getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        TypedQuery<Author> query = getEntityManager()
                .createQuery("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName", Author.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        return query.getSingleResult();
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        return null;
    }

    @Override
    public void deleteAuthorById(Long id) {

    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
```

```java

@Test
void testGetAuthorByName() {
    Author author = authorDao.findAuthorByName("Craig", "Walls");

    assertThat(author).isNotNull();
}
```

## 87 - Save New Author

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class AuthorDaoImpl implements AuthorDoa {
    private final EntityManagerFactory emf;

    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Author getById(Long id) {
        return getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        TypedQuery<Author> query = getEntityManager()
                .createQuery("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName", Author.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        return query.getSingleResult();
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(author);
        em.flush();
        em.getTransaction().commit();
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        return null;
    }

    @Override
    public void deleteAuthorById(Long id) {

    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
```

```java

@Test
void testSaveAuthor() {
    Author author = new Author();
    author.setFirstName("John");
    author.setLastName("Thompson");
    Author saved = authorDao.saveNewAuthor(author);

    assertThat(saved).isNotNull();
    assertThat(saved.getId()).isNotNull();
}
```

## 88 - Update Author

## 89 - Delete Author

      