package chamara.springdatajpasample.sdjpademo.bootstrap;

import chamara.springdatajpasample.sdjpademo.domain.AuthorUuid;
import chamara.springdatajpasample.sdjpademo.domain.Book;
import chamara.springdatajpasample.sdjpademo.repositories.AuthUuidRepository;
import chamara.springdatajpasample.sdjpademo.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "default"})
public class DataInitializer implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final AuthUuidRepository authUuidRepository;
    public DataInitializer(BookRepository bookRepository,AuthUuidRepository authUuidRepository) {
        this.bookRepository = bookRepository;
        this.authUuidRepository = authUuidRepository;

    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Bootstrap started");
        System.out.println("Clearing all data");
        bookRepository.deleteAll();
        System.out.println("Clearing all data completed");

        Book book = new Book("1234", "Spring Framework", "Chamara",null);
        System.out.println("Book ID: " + book.getId());
        Book savedResponse1 = bookRepository.save(book);
        System.out.println("Book ID: " + savedResponse1.getId());

        Book book1 = new Book("12332", "Harry Potter", "JK Rowling",null);
        System.out.println("Book ID: " + book1.getId());
        Book savedResponse2 = bookRepository.save(book1);
        System.out.println("Book ID: " + savedResponse2.getId());

        bookRepository.findAll().forEach(book2 -> {
            System.out.println(book2.getId());
            System.out.println(book2.getTitle());
        });

        AuthorUuid authorUuid = new AuthorUuid();
        authorUuid.setFirstName("Chamara");
        authorUuid.setLastName("Sumanapala");
        AuthorUuid saved = authUuidRepository.save(authorUuid);
        System.out.println("Author ID: " + saved.getId());

    }
}
