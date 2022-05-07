package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;

import java.io.Serializable;

@Entity
@Table(name = "data_administrator")
@DiscriminatorValue("ADMINISTRATOR")
@NamedQueries({
        @NamedQuery(name = "DataAdministrator.findAll", query = "select d from DataAdministrator d"),
        @NamedQuery(name = "DataAdministrator.findById", query = "select d from DataAdministrator d where d.id = :id"),
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataAdministrator extends AccessLevel implements Serializable {
    private static final long serialVersionUID = 1L;

}