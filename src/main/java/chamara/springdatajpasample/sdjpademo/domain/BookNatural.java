package chamara.springdatajpasample.sdjpademo.domain;

import jakarta.persistence.*;

@Entity
public class BookNatural {
    private String isbn;
    @Id
    private String title;
    private String publisher;


    public BookNatural() {

    }
    public BookNatural(String isbn, String title, String publisher) {
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

}
