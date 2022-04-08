package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue()
    @Getter
    private UUID id;

    @Basic(optional = false)
    @Column(name = "version")
    @Version // oznaczenie wersji rekordu w bazie
    @Getter @Setter // stosujemy setter, dopóki nie dowiemy się jak go nie stosować
    private Long version;

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
