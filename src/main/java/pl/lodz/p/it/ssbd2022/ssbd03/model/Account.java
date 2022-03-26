package pl.lodz.p.it.ssbd2022.ssbd03.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.findById", query = "select a from Account a order by a.id"),
        @NamedQuery(name = "Account.findByLogin", query = "select a from Account a order by a.login"),
        @NamedQuery(name = "Account.findByPassword", query = "select a from Account a order by a.password"),
        @NamedQuery(name = "Account.findByConfirmed", query = "select a from Account a order by a.confirmed"),
        @NamedQuery(name = "Account.findByActive", query = "select a from Account a order by a.active"),
        @NamedQuery(name = "Account.findByVersion", query = "select a from Account a order by a.version")
})
@ToString
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @Column(name = "login", unique = true)
    @Getter @Setter
    private String login;

    @ToString.Exclude // Nie chcemy ujawniania skrótu hasła np. w dzienniku zdarzeń
    @Column(name = "password")
    @Getter @Setter
    private String password;

    @Getter @Setter
    private boolean confirmed;



    @Column(name = "active")
    @Getter @Setter
    private boolean active;

    @Column(name = "version")
    @Getter @Setter
    private long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountId")
    private Collection<AccessLevel> poziomDostepuCollection;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    private DataPersonal danePersonalne;

    public Collection<AccessLevel> getAccessLevelCollection() {
        return poziomDostepuCollection;
    }

    public void setPoziomDostepuCollection(Collection<AccessLevel> accessLevelCollection) {
        this.poziomDostepuCollection = poziomDostepuCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}