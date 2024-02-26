package chamara.springdatajpasample.sdjpademo.repositories;

import chamara.springdatajpasample.sdjpademo.domain.composite.AuthorEmbedded;
import chamara.springdatajpasample.sdjpademo.domain.composite.NameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthEmbeddedCompositeRepository extends JpaRepository<AuthorEmbedded, NameId> {
}
