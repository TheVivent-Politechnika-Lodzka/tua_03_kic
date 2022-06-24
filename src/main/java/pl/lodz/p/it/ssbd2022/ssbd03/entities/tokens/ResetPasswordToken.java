package pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name="reset_password_token",
        indexes = {
                @Index(name = "account_id", columnList = "account_id"),
        }
)
@NamedQueries({
        @NamedQuery(name = "ResetPassword.findByLogin", query = "select r from ResetPasswordToken r where r.account.login = :login"),
        @NamedQuery(name = "ResetPassword.findBeforeDate", query = "select r from ResetPasswordToken r where r.expDate < :now")
})
public class ResetPasswordToken implements Serializable {

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
        @Getter
        private Instant expDate;

        @PrePersist
        public void prePersist() {
                this.expDate = Instant.now().plusSeconds(Config.RESET_PASSWORD_TOKEN_EXPIRATION_SECONDS);
        }
}
