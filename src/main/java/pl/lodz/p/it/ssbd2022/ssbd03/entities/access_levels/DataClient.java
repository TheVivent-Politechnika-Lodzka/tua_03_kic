package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Pesel;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "data_client")
@DiscriminatorValue(DataClient.LEVEL_NAME)
@NamedQueries({
        @NamedQuery(name = "DataClient.findAll", query = "SELECT d FROM DataClient d"),
        @NamedQuery(name = "DataClient.findById", query = "select d from DataClient d where d.id = :id"),
        @NamedQuery(name = "DataClient.findByLogin", query = "select d from Account a join a.accessLevelCollection d where a.login = :login"),
        @NamedQuery(name = "DataClient.getLoginFromId", query = "select a.login from Account a join a.accessLevelCollection d where d.id = :id"),
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataClient extends AccessLevel implements Serializable {
    public static final String LEVEL_NAME = "CLIENT";

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{11}$")
    @Column(name = "pesel", nullable = false, length = 11)
    @Getter
    @Setter
    @Pesel
    private String pesel;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must be 9 digits")
    @Column(name = "phone_number", nullable = false, length = 9)
    @Getter
    @Setter
    @PhoneNumber
    private String phoneNumber;

}