package pl.lodz.p.it.ssbd2022.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Rating;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Review;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "implant_review", indexes = {
        @Index(name = "review_client_id", columnList = "client_id"),
        @Index(name = "review_implant_id", columnList = "implant_id")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "implant_id"}, name = ImplantReview.CONSTRAINT_ONE_REVIEW_PER_CLIENT_PER_IMPLANT)
})
@NamedQueries({
        @NamedQuery(name = "Review.findAll", query = "select a from ImplantReview a"),
        @NamedQuery(name = "Review.findById", query = "select a from ImplantReview a where a.id = :id"),
        @NamedQuery(name = "Review.findByClientId", query = "select a from ImplantReview a where a.client.id = :clientId"),
        @NamedQuery(name = "Review.findByImplantId", query = "select a from ImplantReview a where a.implant.id = :implantId")
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
    @Column(name = "review", nullable = false, length = 1000)
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