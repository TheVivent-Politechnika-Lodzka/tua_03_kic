package pl.lodz.p.it.ssbd2022.ssbd03.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;

import java.io.Serializable;
import java.time.Instant;


@Entity
@Table(
        name = "active_account_token",
        indexes = {
                @Index(name = "account_id", columnList = "account_id"),
        }
)
@NamedQueries({
        @NamedQuery(name = "ConfirmationAccountToken.findByLogin", query = "select a from ConfirmationAccountToken a where a.account.login = :login"),
        @NamedQuery(name = "ConfirmationAccountToken.findExpired", query = "select a from ConfirmationAccountToken a where a.expDate < :now"),
})

@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationAccountToken extends AbstractEntity implements Serializable {

    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @OneToOne(optional = false)
    @Getter @Setter
    private Account account;

    @Basic(optional = false)
    @Column(name="confirmation_token", unique = true,nullable = false)
    @Getter
    private String activeToken;

    @Basic(optional = false)
    @Column(name="expdate", nullable = false )
    @Getter
    private Instant expDate;


}
