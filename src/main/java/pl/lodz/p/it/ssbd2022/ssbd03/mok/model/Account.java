package pl.lodz.p.it.ssbd2022.ssbd03.mok.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.client.Client;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Review;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "account")
@SecondaryTable(name = "account_details")
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "select a from Account a"),
        @NamedQuery(name = "Account.findById", query = "select a from Account a where a.id = :id"),
        @NamedQuery(name = "Account.findByLogin", query = "select a from Account a where a.login = :login"),
        @NamedQuery(name = "Account.findByConfirmed", query = "select a from Account a order by a.confirmed"),
        @NamedQuery(name = "Account.findByActive", query = "select a from Account a order by a.active"),
})
@ToString
@NoArgsConstructor
public class Account extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Size(min = 3, max = 20)
    @Column(name = "login", unique = true, nullable = false, length = 20)
    @Getter @Setter
    private String login;

    @Basic(optional = false)
    @ToString.Exclude // Nie chcemy ujawniania skrótu hasła np. w dzienniku zdarzeń
    @Column(name = "password", nullable = false, length = 128)
    @Getter @Setter
    private String password;

    @Basic(optional = false)
    @Column(name = "confirmed", nullable = false)
    @Getter @Setter
    private boolean confirmed;

    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @Getter @Setter
    private boolean active;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "account")
    @Getter
    private Collection<AccessLevel> accessLevelCollection = new ArrayList<>();

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

    // ################ account details ######################

    @Basic(optional = false)
    @Size(min = 3, max = 30)
    @Column(name = "firstName", table = "account_details", nullable = false, length = 30)
    @Getter @Setter
    private String firstName;


    @Basic(optional = false)
    @Size(min = 3, max = 30)
    @Column(name = "surname", table = "account_details", nullable = false, length = 30)
    @Getter @Setter
    private String surname;

    @Basic(optional = false)
    @Email
    @Column(name = "email", table = "account_details", nullable = false, length = 64)
    @Getter @Setter
    private String email;


}