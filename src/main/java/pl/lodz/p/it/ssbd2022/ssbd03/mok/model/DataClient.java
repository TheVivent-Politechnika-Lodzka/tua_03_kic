package pl.lodz.p.it.ssbd2022.ssbd03.mok.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Review;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "data_client")
@DiscriminatorValue("CLIENT")
@NamedQueries({
        @NamedQuery(name = "DataClient.findAll", query = "SELECT d FROM DataClient d"),
        @NamedQuery(name = "DataClient.findById", query = "select d from DataClient d where d.id = :id"),
        @NamedQuery(name = "DataClient.findByPesel", query = "select d from DataClient d where d.pesel = :pesel"),
})
@ToString(callSuper = true)
@NoArgsConstructor @AllArgsConstructor
public class DataClient extends AccessLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{11}$")
    @Column(name = "pesel", nullable = true, length = 11)
    @Getter @Setter
    private String pesel;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "client")
    @Getter
    private Collection<Appointment> appointmentCollection = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "client")
    @Getter
    private Collection<Review> reviewCollection = new ArrayList<>();

}