package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.*;

import java.io.Serializable;
import java.time.Duration;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant.CONSTRAINT_NAME_UNIQUE;

@Entity
@Table(name = "implant",
        indexes = {
                @Index(name = "implant_id", columnList = "id"),
                @Index(name = "implant_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name", name = CONSTRAINT_NAME_UNIQUE),
        }
)
@NamedQueries({
        @NamedQuery(name = "Implant.findAll", query = "select a from Implant a"),
        @NamedQuery(name = "Implant.findById", query = "select a from Implant a where a.id = :id"),
        @NamedQuery(name = "Implant.searchByPhrase", query = "select a from Implant a where lower(a.name) like lower(:phrase) and a.archived = :archived order by a.name asc"),
        @NamedQuery(name = "Implant.searchByPhrase.count", query = "select count(a) from Implant a where lower(a.name) like lower(:phrase) and a.archived = :archived"),
})
@ToString
@NoArgsConstructor
public class Implant extends AbstractEntity implements Serializable {

    public static final String CONSTRAINT_NAME_UNIQUE = "implant_name_unique";
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 50)
    @Getter @Setter
    @Name
    private String name;

    @Basic(optional = false)
    @Column(name = "description", nullable = false, columnDefinition = "TEXT", length = 1000)
    @Getter @Setter
    @Description
    private String description;

    @Basic(optional = false)
    @Column(name = "manufacturer", nullable = false, length = 50)
    @Getter @Setter
    @Manufacturer
    private String manufacturer;

    @Basic(optional = false)
    @Column(name = "price", nullable = false)
    @Getter @Setter
    @Price
    private int price;

    @Basic(optional = false)
    @Column(name = "archived", nullable = false)
    @Getter @Setter
    private boolean archived = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "popularity", referencedColumnName = "id")
    private ImplantPopularityAggregate popularity = new ImplantPopularityAggregate();

    @Basic(optional = false)
    @Column(name = "duration", nullable = false)
    @Getter @Setter
    private Duration duration;

    @Column(name = "image")
    @Getter @Setter
    @Url
    private String image;

    public int getPopularity() {
        return this.popularity.getPopularity();
    }

    public void setPopularity(int popularity) {
        this.popularity.setPopularity(popularity);
    }

}