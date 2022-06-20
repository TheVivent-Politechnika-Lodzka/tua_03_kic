package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;

@Entity
@Table(name = "implant_popularity_aggregate")
@ToString
@NoArgsConstructor
public class ImplantPopularityAggregate extends AbstractEntity {

    @Basic(optional = false)
    @Column(name = "popularity", columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    @Getter
    @Setter
    private int popularity = 0;

    @OneToOne(mappedBy = "popularity")
    private Implant implant;

}
