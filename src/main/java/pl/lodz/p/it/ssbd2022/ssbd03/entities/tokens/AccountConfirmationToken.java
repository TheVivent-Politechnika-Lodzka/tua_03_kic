package pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(
        name = "account_confirmation_token",
        indexes = {
                @Index(name = "account_id", columnList = "account_id"),
        }
)
@NamedQueries({
        @NamedQuery(name = "AccountConfirmationToken.findByLogin", query = "select a from AccountConfirmationToken a where a.account.login = :login"),
        @NamedQuery(name = "AccountConfirmationToken.findExpired", query = "select a from AccountConfirmationToken a where a.expDate < :now"),
})

@AllArgsConstructor
@NoArgsConstructor
public class AccountConfirmationToken implements Serializable {

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

    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @OneToOne(optional = false)
    @Getter @Setter
    private Account account;

    @Basic(optional = false)
    @Column(name="token", unique = true,nullable = false)
    @Getter @Setter
    private String token;

    @Basic(optional = false)
    @Column(name="expdate", nullable = false )
    private Long expDate;

    public Instant getExpDate() {
        return Instant.ofEpochSecond(expDate);
    }

    @PrePersist
    public void prePersist() {
        this.expDate = Instant.now().getEpochSecond()
                + Config.REGISTER_TOKEN_EXPIRATION_SECONDS;
    }


}
