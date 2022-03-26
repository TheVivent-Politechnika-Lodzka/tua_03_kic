package pl.lodz.p.it.ssbd2022.ssbd03.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "access_level")
@ToString
@NamedQueries({
        @NamedQuery(name = "AccessLevel.findAll", query = "SELECT a FROM AccessLevel a"),
        @NamedQuery(name = "AccessLevel.findById", query = "SELECT a FROM AccessLevel a WHERE a.id = :id"),
        @NamedQuery(name = "AccessLevel.findByLevel", query = "select a from AccessLevel a order by a.level"),
        @NamedQuery(name = "AccessLevel.findByActive", query = "select a from AccessLevel a order by a.active"),
        @NamedQuery(name = "AccessLevel.findByVersion", query = "select a from AccessLevel a order by a.version")
})
public class AccessLevel implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "poziom")
    @Getter @Setter
    private String level;

    @Column(name = "aktywny")
    @Getter @Setter
    private boolean active;

    @Column(name = "wersja")
    @Getter @Setter
    private long version;

    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Account accountId;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accessLevel")
    private DataDoctor dataDoctor;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accessLevel")
    private DataClient dataClient;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accessLevel")
    private DataAdministrator dataAdministrator;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccessLevel)) {
            return false;
        }
        AccessLevel other = (AccessLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}