package pl.lodz.p.it.ssbd2022.ssbd03.mok.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "data_administrator")
@DiscriminatorValue("ADMINISTRATOR")
@NamedQueries({
        @NamedQuery(name = "DataAdministrator.findAll", query = "select d from DataAdministrator d"),
        @NamedQuery(name = "DataAdministrator.findById", query = "select d from DataAdministrator d where d.id = :id"),
        @NamedQuery(name = "DataAdministrator.findByPhoneNumber", query = "select d from DataAdministrator d where d.phoneNumber = :phoneNumber"),
})
@ToString(callSuper = true)
@NoArgsConstructor @AllArgsConstructor
public class DataAdministrator extends AccessLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{3}$", message = "Phone number must be 9 digits, separated by '-'")
    @Column(name = "phone_number", length = 11)
    @Getter @Setter
    private String phoneNumber;

}