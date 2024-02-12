package chamara.springdatajpasample.sdjpademo.bootstrap;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import chamara.springdatajpasample.sdjpademo.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookRepository bookRepository;
    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Bootstrap started");
        Book book = new Book("1234", "Spring Framework", "Chamara");
        System.out.println("Book ID: " + book.getId());
        Book savedResponse1 = bookRepository.save(book);
        System.out.println("Book ID: " + savedResponse1.getId());

        Book book1 = new Book("12332", "Harry Potter", "JK Rowling");
        System.out.println("Book ID: " + book1.getId());
        Book savedResponse2 = bookRepository.save(book1);
        System.out.println("Book ID: " + savedResponse2.getId());

        bookRepository.findAll().forEach(book2 -> {
            System.out.println(book2.getId());
            System.out.println(book2.getTitle());
        });
    }
}
