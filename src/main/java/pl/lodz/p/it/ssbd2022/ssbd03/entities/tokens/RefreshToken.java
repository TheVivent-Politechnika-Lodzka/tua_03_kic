package pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.RefreshToken.UNIQUE_TOKEN_CONSTRAINT;

@Entity
@Table(
        name = "refresh_token",
        indexes = {
                @Index(name = "account_id", columnList = "account_id"),
                @Index(name = "token", columnList = "token")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"token"}, name = UNIQUE_TOKEN_CONSTRAINT)
        }
)
@NamedQueries({
        @NamedQuery(name = "RefreshToken.findByLogin", query = "select a from RefreshToken a where a.account.login = :login"),
        @NamedQuery(name = "RefreshToken.findByToken", query = "select a from RefreshToken a where a.token = :token"),
        @NamedQuery(name = "RefreshToken.findExpired", query = "select a from RefreshToken a where a.expDate < :now"),
})

@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken implements Serializable {

        private static final long serialVersionUID = 1L;
        public static final String UNIQUE_TOKEN_CONSTRAINT = "unique_token_constraint";

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
        @ManyToOne(optional = false)
        @Getter @Setter
        private Account account;

        @Basic(optional = false)
        @Column(name="token", unique = true,nullable = false)
        @Getter @Setter
        private String token;

        @Basic(optional = false)
        @Column(name="expdate", nullable = false )
        @Getter
        private Instant expDate;

        @PrePersist
        public void prePersist() {
                this.expDate = Instant.now().plusSeconds(Config.REFRESH_TOKEN_EXPIRATION_SECONDS);
        }
}
