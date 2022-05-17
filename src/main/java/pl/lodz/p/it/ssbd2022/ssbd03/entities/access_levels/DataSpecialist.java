package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "data_specialist")
@DiscriminatorValue(DataSpecialist.LEVEL_NAME)
@NamedQueries({
        @NamedQuery(name = "DataDoctor.findAll", query = "select d from DataSpecialist d"),
        @NamedQuery(name = "DataDoctor.findById", query = "select d from DataSpecialist d where d.id = :id"),
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataSpecialist extends AccessLevel implements Serializable {

    public static final String LEVEL_NAME = "SPECIALIST";

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must be 9 digits")
    @Column(name = "phone_number", nullable = true, length = 9)
    @Getter @Setter
    private String phoneNumber;

    @Basic(optional = false)
    @Email
    @Column(name = "email", nullable = false, length = 128)
    @Getter @Setter
    private String contactEmail;
}