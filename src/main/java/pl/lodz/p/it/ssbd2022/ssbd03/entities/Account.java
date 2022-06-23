package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.FirstName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Account.CONSTRAINT_EMAIL_UNIQUE;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Account.CONSTRAINT_LOGIN_UNIQUE;

@Entity
@Table(
        name = "account",
        indexes = {
                @Index(name = "account_login", columnList = "login"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "login", name = CONSTRAINT_LOGIN_UNIQUE)
        }
)
@SecondaryTable(
        name = "account_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = CONSTRAINT_EMAIL_UNIQUE)
        }
)
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "select a from Account a"),
        @NamedQuery(name = "Account.findById", query = "select a from Account a where a.id = :id"),
        @NamedQuery(name = "Account.findByLogin", query = "select a from Account a where a.login = :login"),
        @NamedQuery(name = "Account.findByConfirmed", query = "select a from Account a order by a.confirmed"),
        @NamedQuery(name = "Account.findByActive", query = "select a from Account a order by a.active"),
        @NamedQuery(name = "Account.searchByPhrase", query = "select a from Account a where lower(concat(a.firstName, ' ', a.lastName)) like lower(:phrase) order by a.lastName, a.firstName asc"),
        @NamedQuery(name = "Account.searchByPhrase.count", query = "select count(a) from Account a where lower(concat(a.firstName, ' ', a.lastName)) like lower(:phrase)"),
})

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity implements Serializable {

    public static final String CONSTRAINT_LOGIN_UNIQUE = "account_login_unique";
    public static final String CONSTRAINT_EMAIL_UNIQUE = "account_email_unique";
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "login", nullable = false, length = 20)
    @Getter
    @Setter
    @Login
    private String login;

    @Basic(optional = false)
    @ToString.Exclude
    @Column(name = "password", nullable = false, length = 128)
    @Getter
    @Setter
    private String password;

    @Basic(optional = false)
    @Column(name = "confirmed", nullable = false)
    @Getter
    @Setter
    @NotNull
    private boolean confirmed;

    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @Getter
    @Setter
    @NotNull
    private boolean active;



    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.ALL}, mappedBy = "account", orphanRemoval = true)
    @Getter
    private Collection<AccessLevel> accessLevelCollection = new ArrayList<>();

    // ################ account details ######################

    @Basic(optional = false)
    @Size(min = 3, max = 30)
    @Column(name = "first_name", table = "account_details", nullable = false, length = 30)
    @Getter
    @Setter
    @FirstName
    private String firstName;

    @Basic(optional = false)
    @Size(min = 3, max = 30)
    @Column(name = "last_name", table = "account_details", nullable = false, length = 30)
    @Getter
    @Setter
    @LastName
    private String lastName;

    @Basic(optional = false)
    @Column(name = "email", table = "account_details", nullable = false, length = 128)
    @Getter
    @Email
    @NotNull
    private String email;
    @Basic(optional = false)
    @Column(name = "language", table = "account_details", nullable = false, length = 16)
    @Getter
    @Setter
    @NotNull
    private Locale language;

    @Column(name = "url", table = "account_details", length = 2000)
    @Size(min = 8, max = 2000)
    @Getter
    @Setter
    @NotNull
    private String url;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void addAccessLevel(AccessLevel accessLevel) {
        accessLevelCollection.add(accessLevel);
        accessLevel.setAccount(this);
    }

    public void removeAccessLevel(AccessLevel accessLevel) {
        accessLevelCollection.remove(accessLevel);
        accessLevel.setAccount(null);
    }

    public boolean isInRole(String role) {
        for (AccessLevel accessLevel : accessLevelCollection) {
            if (accessLevel.getLevel().equals(role)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return login.equals(account.login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }


}