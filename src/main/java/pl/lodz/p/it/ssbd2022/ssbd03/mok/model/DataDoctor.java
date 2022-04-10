package pl.lodz.p.it.ssbd2022.ssbd03.mok.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "data_doctor")
@NamedQueries({
        @NamedQuery(name = "DataDoctor.findAll", query = "SELECT d FROM DataDoctor d"),
        @NamedQuery(name = "DataDoctor.findById", query = "select d from DataDoctor d order by d.id"),
//        @NamedQuery(name = "DataDoctor.findByIntercom", query = "select d from DataDoctor d order by d.intercom"),
        @NamedQuery(name = "DataDoctor.findByPhone", query = "select d from DataDoctor d order by d.phoneNumber"),
//        @NamedQuery(name = "DataDoctor.findByyWersja", query = "select d from DataDoctor d order by d.version")
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataDoctor extends AccessLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{3}$", message = "Phone number must be 9 digits, separated by '-'")
    @Column(name = "phone_number", length = 11)
    @Getter @Setter
    private String phoneNumber;
}