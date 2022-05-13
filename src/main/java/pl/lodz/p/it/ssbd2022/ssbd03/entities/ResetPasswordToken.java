package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;

@Entity
@Table(
        name="reset_password_token",
        indexes = {
                @Index(name = "account_id", columnList = "account_id"),
        }
)
@NamedQueries({
        @NamedQuery(name = "ResetPassword.findByLogin", query = "select r from ResetPasswordToken r where r.account.login = :login"),
})
public class ResetPasswordToken extends AbstractEntity implements java.io.Serializable {

        @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
        @OneToOne(optional = false)
        @Getter @Setter
        private Account account;

}
