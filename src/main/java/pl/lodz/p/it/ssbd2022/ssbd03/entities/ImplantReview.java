package pl.lodz.p.it.ssbd2022.ssbd03.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="review")
@NamedQueries({
        @NamedQuery(name = "Review.findAll", query = "select a from ImplantReview a"),
        @NamedQuery(name = "Review.findById", query = "select a from ImplantReview a where a.id = :id"),
})
@ToString
@NoArgsConstructor
public class ImplantReview extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "implant_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter @Setter
    private Implant implant;

    @JoinColumn(name = "client_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter @Setter
    private DataClient client;

    @Basic(optional = false)
    @Column(name = "text", nullable = false, length = 512)
    @Getter @Setter
    private String text;

    @Basic(optional = false)
    @Column(name = "date", nullable = false)
    @Getter @Setter
    private Date date;
}