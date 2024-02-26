package chamara.springdatajpasample.sdjpademo.repositories;

import chamara.springdatajpasample.sdjpademo.domain.composite.AuthorComposite;
import chamara.springdatajpasample.sdjpademo.domain.composite.NameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCompositeRepository extends JpaRepository<AuthorComposite, NameId> {
}
