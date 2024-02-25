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

    public Book() {

    }
    public Book(String isbn, String title, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
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
