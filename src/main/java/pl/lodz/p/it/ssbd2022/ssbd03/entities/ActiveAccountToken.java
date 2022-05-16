package pl.lodz.p.it.ssbd2022.ssbd03.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(
        name = "active_account_token",
        indexes = {
                @Index(name = "account_id", columnList = "account_id"),
        }
)
@NamedQueries({
        @NamedQuery(name = "ActiveAccountToken.findByLogin", query = "select a from ActiveAccountToken a where a.account.login = :login"),
})

@AllArgsConstructor
@NoArgsConstructor
public class ActiveAccountToken extends AbstractEntity implements Serializable {

    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @OneToOne(optional = false)
    @Getter
    @Setter
    private Account account;

    @Basic(optional = false)
    @Column(name="activetoken", unique = true,nullable = false)
    @Getter
    private String activeToken;

    @Basic(optional = false)
    @Column(name="expdate", nullable = false)
    @Getter
    private Date expDate;


}
