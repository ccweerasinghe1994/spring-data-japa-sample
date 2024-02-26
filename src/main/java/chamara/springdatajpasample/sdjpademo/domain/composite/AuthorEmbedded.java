package chamara.springdatajpasample.sdjpademo.domain.composite;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "author_composite")
public class AuthorEmbedded {
    @EmbeddedId
    private NameId nameId;

    public AuthorEmbedded(NameId nameId) {
        this.nameId = nameId;
    }

    public AuthorEmbedded() {
    }

    public NameId getNameId() {
        return nameId;
    }

    public void setNameId(NameId nameId) {
        this.nameId = nameId;
    }
}
