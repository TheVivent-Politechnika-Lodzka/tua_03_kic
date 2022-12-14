package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "implant_popularity_aggregate")
@ToString
@NoArgsConstructor
public class ImplantPopularityAggregate extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "popularity", columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    @Getter
    @Setter
    private int popularity = 0;

//    @OneToOne(mappedBy = "popularity")
//    private Implant implant;

}
