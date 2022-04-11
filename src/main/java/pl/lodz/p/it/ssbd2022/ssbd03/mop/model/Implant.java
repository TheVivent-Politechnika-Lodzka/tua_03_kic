package pl.lodz.p.it.ssbd2022.ssbd03.mop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "implant")
@NamedQueries({
        @NamedQuery(name = "Implant.findAll", query = "select a from Implant a"),
        @NamedQuery(name = "Implant.findById", query = "select a from Implant a where a.id = :id")
})
@ToString
@NoArgsConstructor
public class Implant extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Size(min = 1, max = 20 )
    @Column(name = "name", unique = true, nullable = false, length = 20)
    @Getter @Setter
    private String name;

    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 1000)
    @Getter @Setter
    private String description;

    @Basic(optional = false)
    @Column(name = "manufacturer", nullable = false, length = 30)
    @Getter @Setter
    private String manufacturer;

    @Basic(optional = false)
    @Column(name = "price", nullable = false)
    @Getter @Setter
    private int price;

    @Basic(optional = false)
    @Column(name = "archived", nullable = false)
    @Getter @Setter
    private boolean archived = false;

    @Basic(optional = false)
    @Column(name = "popularity", nullable = false)
    @Getter @Setter
    private int popularity;

    @Basic(optional = false)
    @Column(name = "duration", nullable = false)
    @Getter @Setter
    private Duration duration;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "implant", fetch = FetchType.LAZY)
    @Getter
    private Collection<Review> reviewCollection = new ArrayList<>();



}