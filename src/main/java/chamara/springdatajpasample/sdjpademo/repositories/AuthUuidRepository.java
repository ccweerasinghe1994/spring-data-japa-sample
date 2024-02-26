package chamara.springdatajpasample.sdjpademo.repositories;

import chamara.springdatajpasample.sdjpademo.domain.AuthorUuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUuidRepository extends JpaRepository<AuthorUuid, Long> {
}
