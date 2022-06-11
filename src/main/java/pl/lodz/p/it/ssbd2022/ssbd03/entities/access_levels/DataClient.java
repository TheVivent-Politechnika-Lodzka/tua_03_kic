package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Pesel;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "data_client")
@DiscriminatorValue(DataClient.LEVEL_NAME)
@NamedQueries({
        @NamedQuery(name = "DataClient.findAll", query = "SELECT d FROM DataClient d"),
        @NamedQuery(name = "DataClient.findById", query = "select d from DataClient d where d.id = :id"),
        @NamedQuery(name = "DataClient.findByLogin", query = "select d from Account a join a.accessLevelCollection d where a.login = :login"),
    })
@ToString(callSuper = true)
@NoArgsConstructor
public class DataClient extends AccessLevel implements Serializable {
    public static final String LEVEL_NAME = "CLIENT";

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{11}$")
    @Column(name = "pesel", nullable = false, length = 11)
    @Getter @Setter
    @Pesel
    private String pesel;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must be 9 digits")
    @Column(name = "phone_number", nullable = false, length = 9)
    @Getter @Setter
    @PhoneNumber
    private String phoneNumber;

}