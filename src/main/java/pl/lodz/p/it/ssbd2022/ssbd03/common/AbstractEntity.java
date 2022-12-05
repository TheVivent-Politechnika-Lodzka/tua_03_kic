package pl.lodz.p.it.ssbd2022.ssbd03.common;

import lombok.Getter;
import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@UuidGenerator(name = "EMP_ID_GEN")
public abstract class AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "EMP_ID_GEN")
    private String id;

    public UUID getId() {
        return UUID.fromString(id);
    }

    @Basic(optional = false)
    @Column(name = "version")
    @Version // oznaczenie wersji rekordu w bazie
    @Getter
    private Long version;

    //pole zawierające datę utworzenia encji
    @Basic(optional = false)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Long createdAt;

    public Instant getCreatedAt() {
        return Instant.ofEpochSecond(createdAt);
    }

    private void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt.getEpochSecond();
    }

    //pole zawierające ostatnio modyfikację encji
    @Basic(optional = false)
    @Column(name = "last_modified", nullable = false)
    private Long lastModified;

    public Instant getLastModified() {
        return Instant.ofEpochSecond(lastModified);
    }

    private void setLastModified(Instant lastModified) {
        this.lastModified = lastModified.getEpochSecond();
    }

    @PrePersist
    public void prePersist() {
        Instant start = Instant.now();
        setCreatedAt(start);
        setLastModified(start);
    }

    @PreUpdate
    public void preUpdate() {
        setLastModified(Instant.now());
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
        return (this.getId() != null || other.getId() == null) && (this.getId() == null || this.getId().equals(other.getId()));
    }
}
