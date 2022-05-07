package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

//    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "specialist")
//    @Getter
//    private Collection<Appointment> appointmentCollection = new ArrayList<>();
}