package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview.CONSTRAINT_ONE_REVIEW_PER_CLIENT_PER_IMPLANT;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Rating;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Review;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "implant_review", indexes = {
        @Index(name = "review_client_id", columnList = "client_id"),
        @Index(name = "review_implant_id", columnList = "implant_id")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "implant_id"}, name = CONSTRAINT_ONE_REVIEW_PER_CLIENT_PER_IMPLANT)
})
@NamedQueries({
        @NamedQuery(name = "Review.findAll", query = "select a from ImplantReview a"),
        @NamedQuery(name = "Review.findById", query = "select a from ImplantReview a where a.id = :id"),
        @NamedQuery(name = "Review.findByClientId", query = "select a from ImplantReview a where a.client.id = :clientId"),
        @NamedQuery(name = "Review.findByImplantId", query = "select a from ImplantReview a where a.implant.id = :implantId order by a.createdAt desc"),
        @NamedQuery(name = "Review.findByImplantId.count", query = "select count(a) from ImplantReview a where a.implant.id = :implantId"),
})
@ToString
@NoArgsConstructor
public class ImplantReview extends AbstractEntity implements Serializable {

    public static final String CONSTRAINT_ONE_REVIEW_PER_CLIENT_PER_IMPLANT = "one_review_per_client_per_implant";
    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "implant_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    @NotNull
    private Implant implant;

    @JoinColumn(name = "client_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    @NotNull
    private Account client;

    @Basic(optional = false)
    @Column(name = "review", nullable = false, length = 1000, columnDefinition = "TEXT")
    @Getter
    @Setter
    @Review
    private String review;

    @Basic(optional = false)
    @Column(name = "rating", nullable = false)
    @Getter
    @Setter
    @Rating
    private double rating;
}