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
@Table(
        name="implant_review",
        indexes = {
                @Index(name = "review_client_id", columnList = "client_id"),
                @Index(name = "review_implant_id", columnList = "implant_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "review_client_id_implant_id", columnNames = {"client_id", "implant_id"})
        }
)
@NamedQueries({
        @NamedQuery(name = "Review.findAll", query = "select a from ImplantReview a"),
        @NamedQuery(name = "Review.findById", query = "select a from ImplantReview a where a.id = :id"),
        @NamedQuery(name = "Review.findByClientId", query = "select a from ImplantReview a where a.client.id = :clientId"),
        @NamedQuery(name = "Review.findByImplantId", query = "select a from ImplantReview a where a.implant.id = :implantId")
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

    @Basic(optional = false)
    @Column(name = "rating", nullable = false)
    @Getter @Setter
    private double rating;
}