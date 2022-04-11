package pl.lodz.p.it.ssbd2022.ssbd03.mok.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Appointment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "data_specialist")
@DiscriminatorValue("SPECIALIST")
@NamedQueries({
        @NamedQuery(name = "DataDoctor.findAll", query = "select d from DataSpecialist d"),
        @NamedQuery(name = "DataDoctor.findById", query = "select d from DataSpecialist d where d.id = :id"),
        @NamedQuery(name = "DataDoctor.findByPhoneNumber", query = "select d from DataSpecialist d where d.phoneNumber = :phoneNumber"),
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataSpecialist extends AccessLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{3}$", message = "Phone number must be 9 digits, separated by '-'")
    @Column(name = "phone_number", nullable = true, length = 11)
    @Getter @Setter
    //TODO: pager?
    private String phoneNumber;

//    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "account")
//    @Getter
//    private Collection<AccessLevel> accessLevelCollection = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "specialist")
    @Getter
    private Collection<Appointment> appointmentCollection = new ArrayList<>();
}