package pl.lodz.p.it.ssbd2022.ssbd03.common;

import javax.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue()
    @Getter
    private UUID id;

    @Basic(optional = false)
    @Column(name = "version")
    @Version // oznaczenie wersji rekordu w bazie
    @Getter
    private Long version;

    //pole zawierające datę utworzenia encji
    @Basic(optional = false)
    @Column(name = "created_at", updatable = false, nullable = false)
    @Getter
    private Instant createdAt;

    //pole zawierające ostatnio modyfikację encji
    @Basic(optional = false)
    @Column(name = "last_modified", nullable = false)
    @Getter
    private Instant lastModified;

    @PrePersist
    public void prePersist() {
        Instant start = Instant.now();
        createdAt = start;
        lastModified = start;
    }

    @PreUpdate
    public void preUpdate() {
        lastModified = Instant.now();
    }

    // implementacja equals i hashCode,
    // dzięki temu nie trzeba implementować w każdej encji
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
}
