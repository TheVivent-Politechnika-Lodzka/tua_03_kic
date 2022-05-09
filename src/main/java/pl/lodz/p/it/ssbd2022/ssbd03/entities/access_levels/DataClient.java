package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "data_client")
@DiscriminatorValue(DataClient.LEVEL_NAME)
@NamedQueries({
        @NamedQuery(name = "DataClient.findAll", query = "SELECT d FROM DataClient d"),
        @NamedQuery(name = "DataClient.findById", query = "select d from DataClient d where d.id = :id"),
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataClient extends AccessLevel implements Serializable {
    public static final String LEVEL_NAME = "CLIENT";

    private static final long serialVersionUID = 1L;

//    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "client")
//    @Getter
//    private Collection<Appointment> appointmentCollection = new ArrayList<>();
//
//    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "client")
//    @Getter
//    private Collection<ImplantReview> implantReviewCollection = new ArrayList<>();

}