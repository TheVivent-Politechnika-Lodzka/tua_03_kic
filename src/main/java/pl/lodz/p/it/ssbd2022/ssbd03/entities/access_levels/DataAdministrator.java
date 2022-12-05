package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Column(name = "phone_number", nullable = true, length = 9)
    @Getter
    @Setter
    @PhoneNumber
    private String phoneNumber;

    @Basic(optional = false)
    @Column(name = "email", nullable = false, length = 128)
    @Getter
    @Setter
    @Email
    @NotNull
    private String contactEmail;

}