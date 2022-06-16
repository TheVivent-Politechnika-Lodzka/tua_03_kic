package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(
        name = "appointment",
        indexes = {
                @Index(name = "appointment_client_id", columnList = "client_id"),
                @Index(name = "appointment_specialist_id", columnList = "specialist_id"),
                @Index(name = "appointment_implant_id", columnList = "implant_id"),
        }
)
@NamedQueries({
        @NamedQuery(name = "Appointment.findAll", query = "select a from Appointment a"),
        @NamedQuery(name = "Appointment.findById", query = "select a from Appointment a where a.id = :id"),
        @NamedQuery(name = "Appointment.findByClientId", query = "select a from Appointment a where a.client.id = :clientId"),
        @NamedQuery(name = "Appointment.findBySpecialistId", query = "select a from Appointment a where a.specialist.id = :specialistId"),
        @NamedQuery(name = "Appointment.findByClientLogin", query = "select a from Appointment a where a.client.login = :login "),
        @NamedQuery(name = "Appointment.findByLogin", query = "select a from Appointment a where a.client.login = :login or a.specialist.login = :login"),
        @NamedQuery(name = "Appointment.findBetweenDates", query = "select a from Appointment a where a.startDate >= :startDate and a.startDate <= :endDate"),
        @NamedQuery(name = "Appointment.searchByPhrase", query = "select a from Appointment a where a.client.login like concat('%', :phrase, '%') or a.specialist.login like concat('%', :phrase, '%')"),
})
@ToString
@NoArgsConstructor
public class Appointment extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "client_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private Account client;

    @JoinColumn(name = "specialist_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private Account specialist;

    @JoinColumn(name = "implant_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @Getter
    @Setter
    private Implant implant;

    @Basic(optional = false)
    @Column(name = "startDate", nullable = false)
    @Getter
    @Setter
    private Date startDate;

    @Basic(optional = false)
    @Column(name = "endDate", nullable = false)
    @Getter
    @Setter
    private Date endDate;

    @Basic(optional = false)
    @Column(name = "price", nullable = false)
    @Getter
    @Setter
    private int price;

    @Basic(optional = false)
    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    private String description;

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private Status status;

}