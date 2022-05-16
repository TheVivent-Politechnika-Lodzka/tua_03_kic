package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;

import java.io.Serializable;

@Entity
@Table(name = "data_administrator")
@DiscriminatorValue(DataAdministrator.LEVEL_NAME)
@NamedQueries({
        @NamedQuery(name = "DataAdministrator.findAll", query = "select d from DataAdministrator d"),
        @NamedQuery(name = "DataAdministrator.findById", query = "select d from DataAdministrator d where d.id = :id"),
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataAdministrator extends AccessLevel implements Serializable {
    public static final String LEVEL_NAME = "ADMINISTRATOR";

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must be 9 digits")
    @Column(name = "phone_number", nullable = false, length = 9)
    @Getter @Setter
    private String phoneNumber;

    @Basic(optional = false)
    @Email
    @Column(name = "email", nullable = false, length = 128)
    @Getter @Setter
    private String contactEmail;

}