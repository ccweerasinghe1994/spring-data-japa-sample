package chamara.springdatajpasample.sdjpademo.repositories;

import chamara.springdatajpasample.sdjpademo.domain.BookUuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookUuidRepository extends JpaRepository<BookUuid, Long> {
}
