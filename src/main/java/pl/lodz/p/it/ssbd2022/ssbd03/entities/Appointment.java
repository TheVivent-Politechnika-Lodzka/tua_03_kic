package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Description;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Manufacturer;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Name;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Price;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@Entity
@Table(
        name = "appointment",
        indexes = {
                @Index(name = "appointment_client_id", columnList = "client_id"),
                @Index(name = "appointment_specialist_id", columnList = "specialist_id"),
                @Index(name = "appointment_implant_id", columnList = "implant_id"),
        }
)
@SecondaryTable(name = "implant_backup_in_appointment")
@NamedQueries({
        @NamedQuery(name = "Appointment.findAll", query = "select a from Appointment a"),
        @NamedQuery(name = "Appointment.findById", query = "select a from Appointment a where a.id = :id"),
        @NamedQuery(name = "Appointment.findByClientId", query = "select a from Appointment a where a.client.id = :clientId"),
        @NamedQuery(name = "Appointment.findBySpecialistId", query = "select a from Appointment a where a.specialist.id = :specialistId"),
        @NamedQuery(name = "Appointment.findByClientLogin", query = "select a from Appointment a where a.client.login = :login"),
        @NamedQuery(name = "Appointment.findSpecialistAppointmentsInGivenPeriod", query =
                "select a from Appointment a where " +
                        "a.specialist.id = :specialistId and (" +
                        "(a.startDate >= :startDate and a.startDate < :endDate) or" +
                        "(a.endDate > :startDate and a.endDate <= :endDate)" +
                        ")" +
                        "order by a.startDate, a.endDate asc"
        ), // nie mogę użyć BETWEEN, ponieważ JPA nie wspiera BETWEEN na Instant
        @NamedQuery(name = "Appointment.findSpecialistAppointmentsInGivenPeriod.count", query =
                "select count(a) from Appointment a where "+
                "a.specialist.id = :specialistId and (" +
                    "(a.startDate >= :startDate and a.startDate < :endDate) or" +
                    "(a.endDate > :startDate and a.endDate <= :endDate)" +
                ")"
                ), // nie mogę użyć BETWEEN, ponieważ JPA nie wspiera BETWEEN na Instant
        @NamedQuery(name = "Appointment.findByLogin", query = "select a from Appointment a where a.client.login = :login or a.specialist.login = :login"),
        @NamedQuery(name = "Appointment.findBetweenDates", query = "select a from Appointment a where a.startDate >= :startDate and a.startDate <= :endDate"),
        @NamedQuery(name = "Appointment.searchByPhrase", query = "select a from Appointment a where a.client.login like concat('%', :phrase, '%') or a.specialist.login like concat('%', :phrase, '%') order by a.startDate, a.endDate asc"),
        @NamedQuery(name = "Appointment.searchByPhrase.count", query = "select count(a) from Appointment a where a.client.login like concat('%', :phrase, '%') or a.specialist.login like concat('%', :phrase, '%')"),
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
    private Implant implant;

    @Basic(optional = false)
    @Column(name = "startDate", nullable = false)
    @Getter
    @Setter
    private Instant startDate;

    @Basic(optional = false)
    @Column(name = "endDate", nullable = false)
    @Getter
    @Setter
    private Instant endDate;

    @Basic(optional = false)
    @Column(name = "price", nullable = false)
    @Getter
    @Setter
    private int price;

    @Basic(optional = false)
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    @Getter
    @Setter
    private String description;

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private Status status = Status.PENDING; // wartość domyślna dla nowych wizyt

    // ################ backed up implant details ######################

    @Basic(optional = false)
    @Column(name = "name", nullable = false, table = "implant_backup_in_appointment", length = 50, updatable = false)
    @Getter
    @Name
    private String implantName;

    @Basic(optional = false)
    @Column(name = "description", nullable = false, table = "implant_backup_in_appointment", updatable = false, columnDefinition = "TEXT")
    @Getter
    @Description
    private String implantDescription;

    @Basic(optional = false)
    @Column(name = "manufacturer", nullable = false, table = "implant_backup_in_appointment", updatable = false)
    @Getter
    @Manufacturer
    private String implantManufacturer;

    @Basic(optional = false)
    @Column(name = "price", nullable = false, table = "implant_backup_in_appointment", updatable = false)
    @Getter
    @Price
    private int implantPrice;

    @Basic(optional = false)
    @Column(name = "duration", nullable = false, table = "implant_backup_in_appointment", updatable = false)
    @Getter
    private Duration implantDuration;

    // własny setter dla lepszej spójności danych
    public void setImplant(Implant implant) {
        this.implant = implant;
        implantDescription = implant.getDescription();
        implantDuration = implant.getDuration();
        implantManufacturer = implant.getManufacturer();
        implantName = implant.getName();
        implantPrice = implant.getPrice();
    }
}