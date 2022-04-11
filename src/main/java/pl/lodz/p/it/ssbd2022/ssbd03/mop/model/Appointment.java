package pl.lodz.p.it.ssbd2022.ssbd03.mop.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.DataSpecialist;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="appointment")
@NamedQueries({
        @NamedQuery(name = "Appointment.findAll", query = "select a from Appointment a"),
        @NamedQuery(name = "Appointment.findById", query = "select a from Appointment a where a.id = :id"),
})
@ToString
@NoArgsConstructor
public class Appointment extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

//    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
//    @ManyToOne(optional = false)
//    @Getter @Setter
//    private Account account;


    @JoinColumn(name = "specialist_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter @Setter
    DataSpecialist specialist;

    @JoinColumn(name = "client_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter @Setter
    DataClient client;

    @JoinColumn(name = "implant_id", referencedColumnName = "id", updatable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    @Getter @Setter
    Implant implant;

    @Basic(optional = false)
    @Column(name = "startDate", nullable = false)
    @Getter @Setter
    Date startDate;

    @Basic(optional = false)
    @Column(name = "endDate", nullable = false)
    @Getter @Setter
    Date endDate;

    @Basic(optional = false)
    @Column(name = "price", nullable = false)
    @Getter @Setter
    int price;

    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 255)
    @Getter @Setter
    String description;

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    Status status;


}