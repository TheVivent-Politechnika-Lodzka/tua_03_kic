package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

import java.io.Serializable;

@Entity
@Table(name = "data_specialist")
@DiscriminatorValue(DataSpecialist.LEVEL_NAME)
@NamedQueries({
        @NamedQuery(name = "DataSpecialist.findAll", query = "select d from DataSpecialist d"),
        @NamedQuery(name = "DataSpecialist.findById", query = "select d from DataSpecialist d where d.id = :id"),
        @NamedQuery(name = "DataSpecialist.searchSpecialistByPhrase", query = "select d.account from DataSpecialist d where" +
                " lower(concat(d.account.firstName, ' ', d.account.lastName)) like lower(:phrase) order by d.account.lastName, d.account.firstName asc"),
        @NamedQuery(name = "DataSpecialist.searchSpecialistByPhrase.count", query = "select count(d.account) from DataSpecialist d where" +
                " lower(concat(d.account.firstName, ' ', d.account.lastName)) like lower(:phrase)"),
})

@ToString(callSuper = true)
@NoArgsConstructor
public class DataSpecialist extends AccessLevel implements Serializable {

    public static final String LEVEL_NAME = "SPECIALIST";

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